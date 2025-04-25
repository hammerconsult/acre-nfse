package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ImpressaoServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.ServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.ServicoDeclaradoSearchNfseDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.ServicoDeclaracaoSearchMapper;
import br.com.webpublico.repository.mapper.ServicoDeclaradoMapper;
import br.com.webpublico.repository.setter.ServicoDeclaradoSetter;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ServicoDeclaradoJDBCRepository implements Serializable {

    private final static String SELECT_SERVICO_DECLARADO_SEARCH = " select sd.id as id, " +
            "        sd.numero as numero, " +
            "        sd.emissao as emissao, " +
            "        sd.tiposervicodeclarado as tipo_servico, " +
            "        tsd.codigo as tipo_documento_codigo, " +
            "        tsd.descricao as tipo_documento_descricao, " +
            "        dpt.cpfcnpj as tomador_cpf_cnpj," +
            "        dpt.nomerazaosocial as tomador_nome_razao, " +
            "        dpp.cpfcnpj as prestador_cpf_cnpj," +
            "        dpp.nomerazaosocial as prestador_nome_razao, " +
            "        dec.issretido as iss_retido, " +
            "        dec.totalservicos as total_servico, " +
            "        dec.basecalculo as base_calculo, " +
            "        dec.isscalculado as iss_calculado," +
            "        dec.situacao as situacao," +
            "        dec.naturezaoperacao as naturezaoperacao  ";
    private final static String FROM_SERVICO_DECLARADO_SEARCH = "     from declaracaoprestacaoservico dec " +
            "  inner join itemdeclaracaoservico ids on ids.declaracaoprestacaoservico_id = dec.id " +
            "  inner join servico s on s.id = ids.servico_id " +
            "  left join cidade c on c.id = ids.municipio_id " +
            "  left join uf uf on uf.id = c.uf_id " +
            "  inner join servicodeclarado sd on dec.id = sd.declaracaoprestacaoservico_id " +
            "  inner join cadastroeconomico ce on ce.id = sd.cadastroeconomico_id " +
            "  left join tipodocservicodeclarado tsd on tsd.id = sd.tipodocservicodeclarado_id " +
            "  left join dadospessoaisnfse dpt on dpt.id = dec.dadospessoaistomador_id " +
            "  left join dadospessoaisnfse dpp on dpp.id = dec.dadospessoaisprestador_id ";
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


    public List<ServicoDeclaradoSearchNfseDTO> consultarServicosDeclarados(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT_SERVICO_DECLARADO_SEARCH)
                .append(FROM_SERVICO_DECLARADO_SEARCH);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        if (Strings.isNullOrEmpty(orderBy)) {
            orderBy = " ORDER BY sd.numero DESC ";
        }
        sql.append(orderBy);
        if (pageable != null) {
            parameters.addValue("offset", pageable.getOffset());
            parameters.addValue("limit", pageable.getPageSize());
            sql.append(" offset :offset rows fetch first :limit rows only ");
        }
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new ServicoDeclaracaoSearchMapper());
    }

    public Integer contarServicos(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(sd.id) as value ").append(FROM_SERVICO_DECLARADO_SEARCH);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public ServicoDeclaradoNfseDTO inserir(ServicoDeclaradoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into servicodeclarado (id, cadastroeconomico_id, emissao, numero, declaracaoprestacaoservico_id, " +
                "                              tiposervicodeclarado, tipodocservicodeclarado_id) " +
                " values (?, ?, ?, ?, ?, ?, ?) ", new ServicoDeclaradoSetter(dto));
        return dto;
    }

    public ServicoDeclaradoNfseDTO findById(Long id) {
        List<ServicoDeclaradoNfseDTO> query = jdbcTemplate.query(" select sd.id, " +
                " sd.cadastroeconomico_id, sd.emissao, sd.numero, " +
                " sd.declaracaoprestacaoservico_id, sd.tiposervicodeclarado, sd.tipodocservicodeclarado_id " +
                "    from servicodeclarado sd " +
                "  inner join declaracaoprestacaoservico dec on dec.id = sd.declaracaoprestacaoservico_id " +
                " where sd.id = ? ", new Object[]{id}, new ServicoDeclaradoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public ImpressaoServicoDeclaradoNfseDTO buscarDadosImpressaoServicoDeclarado(Long idServicoDeclarado) {
        String sql = " select " +
                "        ce.inscricaoCadastral as tomadorCmc, " +
                "        dpt.nomeRazaoSocial as tomadorRazaoSocial, " +
                "        dpt.cpfCnpj as tomadorCpfCnpj, " +
                "        dpp.inscricaoMunicipal as prestadorCmc, " +
                "        dpp.nomeRazaoSocial as prestadorRazaoSocial, " +
                "        dpp.cpfCnpj as prestadorCpfCnpj, " +
                "        sd.numero as numero, " +
                "        sd.emissao as emissao, " +
                "        tsd.descricao as tipoDocumento, " +
                "        c.nome as municipio, " +
                "        s.codigo as item, " +
                "        s.nome as servico, " +
                "        ids.descricao as discriminacaoServico, " +
                "        ids.valorServico as valorServico, " +
                "        dec.deducoesLegais as deducao, " +
                "        dec.baseCalculo as baseCalculo, " +
                "        ids.aliquotaServico as aliquota, " +
                "        ids.iss as imposto, " +
                "        dec.situacao as situacao " +
                "      from declaracaoprestacaoservico dec " +
                "     inner join itemdeclaracaoservico ids on ids.declaracaoprestacaoservico_id = dec.id " +
                "     inner join servico s on s.id = ids.servico_id " +
                "     left join cidade c on c.id = ids.municipio_id " +
                "     left join uf uf on uf.id = c.uf_id " +
                "     inner join servicodeclarado sd on dec.id = sd.declaracaoprestacaoservico_id " +
                "     inner join cadastroeconomico ce on ce.id = sd.cadastroeconomico_id " +
                "     left join tipodocservicodeclarado tsd on tsd.id = sd.tipodocservicodeclarado_id " +
                "     left join dadospessoaisnfse dpt on dpt.id = dec.dadospessoaistomador_id " +
                "     left join dadospessoaisnfse dpp on dpp.id = dec.dadospessoaisprestador_id " +
                " where sd.id = ? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{idServicoDeclarado}, new ImpressaoServicoDeclaradoNfseDTO());
    }

    public List<Long> buscarIdCalculoServicoDeclarado(ServicoDeclaradoNfseDTO servico) {
        String sql = "select calculo.id " +
                "  from declaracaoprestacaoservico dec  " +
                " inner join itemdeclaracaoservico item on dec.id = item.declaracaoprestacaoservico_id  " +
                " inner join servicodeclarado sd on dec.id = sd.declaracaoprestacaoservico_id  " +
                " inner join notadeclarada notadec on notadec.declaracaoprestacaoservico_id = dec.id " +
                " inner join declaracaomensalservico dms on dms.id = notadec.declaracaomensalservico_id " +
                " inner join processocalculo proc on proc.id = dms.processocalculo_id " +
                " inner join calculo on calculo.processocalculo_id = proc.id " +
                " where sd.id = :idservico ";
        if (servico.getDeclaracaoPrestacaoServico().getIssRetido()) {
            sql += " and dms.tipomovimento in (:retencao, :iss_retido) ";
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idservico", servico.getId());
        if (servico.getDeclaracaoPrestacaoServico().getIssRetido()) {
            parameters.addValue("retencao", TipoMovimentoMensalNfseDTO.RETENCAO.name());
            parameters.addValue("iss_retido", TipoMovimentoMensalNfseDTO.ISS_RETIDO.name());
        }

        return namedParameterJdbcTemplate.queryForList(sql, parameters, Long.class);
    }

    public void delete(ServicoDeclaradoNfseDTO servicoDeclarado) {
        jdbcTemplate.update(" delete from servicodeclarado where id = ? ", servicoDeclarado.getId());
    }
}
