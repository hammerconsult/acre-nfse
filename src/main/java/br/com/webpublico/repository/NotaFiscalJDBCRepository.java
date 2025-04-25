package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.LongMapper;
import br.com.webpublico.repository.mapper.NotaFiscalEmailSearchMapper;
import br.com.webpublico.repository.mapper.StringMapper;
import br.com.webpublico.repository.setter.NotaFiscalSetter;
import br.com.webpublico.util.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class NotaFiscalJDBCRepository extends AbstractJDBCRepository<NotaFiscalNfseDTO> {

    @Override
    public String getSelect() {
        return "select " +
                "       obj.id,  " +
                "       obj.numero,  " +
                "       obj.codigoverificacao,  " +
                "       obj.declaracaoprestacaoservico_id,  " +
                "       obj.emissao,  " +
                "       obj.emails,  " +
                "       obj.tomador_id,  " +
                "       obj.prestador_id,  " +
                "       obj.descriminacaoservico,  " +
                "       obj.chaveacesso,  " +
                "       obj.homologacao,  " +
                "       obj.informacoesadicionais,  " +
                "       obj.substitutotributario,  " +
                "       obj.enviouporemail," +
                "       dps.situacao, " +
                "       dps.modalidade, " +
                "       dps.totalservicos, " +
                "       dps.deducoeslegais, " +
                "       dps.descontoscondicionais, " +
                "       dps.descontosincondicionais, " +
                "       dps.retencoesfederais, " +
                "       dps.basecalculo, " +
                "       dps.isscalculado, " +
                "       dps.isspagar, " +
                "       dps.valorliquido, " +
                "       ids.aliquotaservico, " +
                "       rps.id as rps_id," +
                "       dps.totalnota as totalnota ";
    }

    @Override
    public String getFrom() {
        return " from notafiscal obj " +
                " inner join cadastroeconomico ce on ce.id = obj.prestador_id" +
                " left join rps rps on rps.id = obj.rps_id" +
                " inner join declaracaoprestacaoservico dps on dps.id = obj.declaracaoprestacaoservico_id" +
                " inner join itemdeclaracaoservico ids on ids.declaracaoprestacaoservico_id = dps.id " +
                " left join dadospessoaisnfse dptnf on dptnf.id = dps.dadospessoaistomador_id " +
                " left join dadospessoaisnfse dppnf on dppnf.id = dps.dadospessoaisprestador_id ";
    }

    @Override
    public RowMapper<NotaFiscalNfseDTO> newRowMapper() {
        return new NotaFiscalNfseDTO();
    }

    @Override
    public NotaFiscalNfseDTO insert(NotaFiscalNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into notafiscal (id, codigoverificacao, declaracaoprestacaoservico_id, " +
                " emissao, emails, rps_id, tomador_id, prestador_id, descriminacaoservico, chaveacesso, homologacao, " +
                " informacoesadicionais, substitutotributario, migrado, enviouporemail) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", new NotaFiscalSetter(dto));
        return dto;
    }

    @Override
    public NotaFiscalNfseDTO update(NotaFiscalNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(NotaFiscalNfseDTO dto) {
    }

    public boolean hasNotaParaRps(String numero, Long idPrestador) {
        List<Integer> query = jdbcTemplate.query(" select 1 as value from notafiscal nf " +
                        " inner join rps rps on rps.id = nf.rps_id " +
                        " where rps.numero = ? and rps.prestador_id = ? ",
                new Object[]{numero, idPrestador}, new IntegerMapper());
        return query != null && !query.isEmpty();
    }

    public Long getNumeroByNota(Long notaId) {
        List<Long> query = jdbcTemplate.query(" select numero as value from notafiscal nf " +
                        " where nf.id = ? ",
                new Object[]{notaId}, new LongMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0L;
    }

    public Date getUltimaEmissao(Long prestadorId) {
        try {
            Date ultimaEmissao = jdbcTemplate.queryForObject(" select max(emissao) as value from notafiscal nf " +
                            " inner join declaracaoprestacaoservico dps on dps.id = nf.declaracaoprestacaoservico_id " +
                            " where nf.prestador_id = ? and dps.situacao != ? ",
                    new Object[]{prestadorId, "CANCELADA"}, Date.class);
            return ultimaEmissao;

        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public Integer getDiaVencimentoIss() {
        List<Integer> query = jdbcTemplate.query(" select pfp.diavencimento as value " +
                        "   from configuracaonfse c  " +
                        "  inner join configuracaonfsedivida cd on cd.configuracaonfse_id = c.id  " +
                        "  inner join opcaopagamentodivida opd on opd.divida_id = cd.dividanfse_id  " +
                        "  inner join opcaopagamento op on op.id = opd.opcaopagamento_id  " +
                        "  inner join parcela p on p.opcaopagamento_id = op.id  " +
                        "  inner join parcelafixaperiodica pfp on pfp.id = p.id  " +
                        " where cd.tipomovimentomensal = ?" +
                        "  and cd.tipodeclaracaomensalservico = ?  " +
                        "  and current_date between coalesce(opd.iniciovigencia, current_date)  " +
                        "  and coalesce(opd.finalvigencia, current_date) ",
                new Object[]{TipoMovimentoMensalNfseDTO.NORMAL.name(), TipoDeclaracaoMensalServicoNfseDTO.PRINCIPAL.name()},
                new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<Long> buscarIdsNotasFiscais(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select obj.id as value ")
                .append(getFrom());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new LongMapper());
    }

    public List<NotaFiscalNfseDTO> consultarNotasFiscaisParaEnviarPorEmail() {
        return namedParameterJdbcTemplate.query("select nota.id as id, nota.homologacao as homologacao, nota.emails as email, nota.numero as numero" +
                        "  from NotaFiscal nota where nota.enviouPorEmail = 0 fetch first 100 rows only ",
                new NotaFiscalEmailSearchMapper());
    }

    public NotaFiscalNfseDTO findByInscricaoCadastralAndNumero(String inscricaoCadastral, Long numero, boolean homologa) {
        List<NotaFiscalNfseDTO> query = jdbcTemplate.query(getSelect() + getFrom() +
                        " where ce.inscricaocadastral = ? and obj.numero = ? and obj.homologacao = ? ",
                new Object[]{inscricaoCadastral, numero, homologa}, new NotaFiscalNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public NotaFiscalNfseDTO buscarNfsePorAutenticacao(AutenticaNfseDTO autenticaNfse) {

        StringBuilder sql = new StringBuilder();
        sql.append("select notafiscal.id ")
                .append(" from notafiscal ")
                .append(" inner join declaracaoprestacaoservico dec on dec.id = notafiscal.declaracaoprestacaoservico_id ")
                .append(" inner join dadospessoaisnfse dadosPrestador on dadosPrestador.id = dec.dadospessoaisprestador_id ")
                .append(" where dadosPrestador.cpfCnpj = :cpfCnpj ")
                .append(" and notafiscal.numero = :numero ")
                .append(" and upper(regexp_replace(notafiscal.codigoVerificacao, '[^a-zA-Z0-9]', '')) = :codigoVerificacao  ");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("numero", Integer.valueOf(autenticaNfse.getNumeroNfse()));
        parameters.addValue("cpfCnpj", autenticaNfse.getCpfCnpjPrestador().replaceAll("[^a-zA-Z0-9]", ""));
        parameters.addValue("codigoVerificacao", autenticaNfse.getCodigoAutenticacao().replaceAll("[^a-zA-Z0-9]", "").toUpperCase());

        List<Long> longs = namedParameterJdbcTemplate.queryForList(sql.toString(), parameters, Long.class);
        if (!longs.isEmpty()) {
            return findById(longs.get(0));
        } else {
            return null;
        }
    }

    public NotaFiscalNfseDTO findByIdPrestadorAndNumero(Long idPrestador, Long numero) {
        List<NotaFiscalNfseDTO> query = jdbcTemplate.query(getSelect() + getFrom() +
                " where ce.id = ? and obj.numero = ? ", new Object[]{idPrestador, numero}, new NotaFiscalNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public void pagarNotaFiscal(NotaFiscalNfseDTO nota) {
        jdbcTemplate.batchUpdate(" update declaracaoprestacaoservico set situacao = 'PAGA' where id =  " +
                nota.getDeclaracaoPrestacaoServico().getId());
    }

    public List<NotaFiscalNfseDTO> buscarNotasASeremPagas() {
        List<NotaFiscalNfseDTO> query = jdbcTemplate.query(
                "select nota.id as id_nota, dps.id as id_declaracao " +
                        "from notafiscal nota " +
                        "         inner join declaracaoprestacaoservico dps on nota.declaracaoprestacaoservico_id = dps.id " +
                        "         inner join dadospessoaisnfse prestador on prestador.id = dps.dadospessoaisprestador_id " +
                        "         inner join notadeclarada nd on nd.declaracaoprestacaoservico_id = dps.id " +
                        "         inner join declaracaomensalservico dms on nd.declaracaomensalservico_id = dms.id " +
                        "         inner join processocalculo p on p.id = dms.processocalculo_id " +
                        "         inner join calculo on calculo.processocalculo_id = p.id " +
                        "         inner join valordivida vd on vd.calculo_id = calculo.id " +
                        "         inner join parcelavalordivida pvd on pvd.valordivida_id = vd.id " +
                        "         inner join situacaoparcelavalordivida spvd on spvd.id = pvd.situacaoatual_id " +
                        "         inner join itemdam on itemdam.parcela_id = pvd.id " +
                        "         inner join dam on dam.id = itemdam.dam_id " +
                        "  where dms.id > 0 " +
                        "  and spvd.situacaoparcela = 'PAGO' " +
                        "  and dps.situacao = 'EMITIDA'" +
                        "  and ((dms.tipomovimento = 'NORMAL' and dps.issretido = 0) or (dms.tipomovimento = 'RETENCAO' and dps.issretido = 1))"
                , (resultSet, i) -> {
                    NotaFiscalNfseDTO dto = new NotaFiscalNfseDTO();
                    dto.setId(resultSet.getLong("id_nota"));
                    dto.setDeclaracaoPrestacaoServico(new DeclaracaoPrestacaoServicoNfseDTO());
                    dto.getDeclaracaoPrestacaoServico().setId(resultSet.getLong("id_declaracao"));
                    return dto;
                });
        return query;
    }

    public List<NotaFiscalNfseDTO> findByLoteRps(Long idLoteRps) {
        return jdbcTemplate.query(getSelect() + getFrom() +
                " where rps.loterps_id = ? ", new Object[]{idLoteRps}, new NotaFiscalNfseDTO());
    }

    public void defineEmailEnviado(NotaFiscalNfseDTO notaFiscal) {
        jdbcTemplate.batchUpdate(" update notafiscal set enviouporemail = 1  where id =  " + notaFiscal.getId());
    }

    public EstatisticaMensalNfseDTO buscarEstatisticaMensalNotasRecebidas(Integer mes, Integer ano, String cpf) {
        String sql = " select " +
                "       extract(month from dps.competencia) as mes, " +
                "       extract(year from dps.competencia) as ano," +
                "       count(case when dps.situacao != :cancelada then 1 else 0 end) as emitidas, " +
                "       count(case when dps.situacao = :cancelada then 1 else 0 end) as canceladas, " +
                "       sum(coalesce(dps.totalservicos, 0)) as totalservicos, " +
                "       sum(coalesce(dps.isscalculado, 0)) as totalpagar " +
                "   from declaracaoprestacaoservico dps" +
                "  inner join dadospessoaisnfse dpt on dpt.id = dps.dadospessoaistomador_id " +
                " where dpt.cpfcnpj = :cpf " +
                "   and extract(year from dps.competencia) = :ano" +
                "   and extract(month from dps.competencia) = :mes" +
                " group by extract(month from dps.competencia), " +
                "          extract(year from dps.competencia) " +
                " order by extract(year from dps.competencia), " +
                "          extract(month from dps.competencia) ";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cpf", cpf);
        parameters.addValue("mes", mes);
        parameters.addValue("ano", ano);
        parameters.addValue("cancelada", SituacaoDeclaracaoNfseDTO.CANCELADA.name());

        EstatisticaMensalNfseDTO estatistica = new EstatisticaMensalNfseDTO(mes, ano);

        namedParameterJdbcTemplate.query(sql, parameters, (ResultSetExtractor<EstatisticaMensalNfseDTO>) rs -> {
            if (rs.next()) {
                estatistica.setEmitidas(rs.getInt("emitidas"));
                estatistica.setCanceladas(rs.getInt("canceladas"));
                estatistica.somarServico(rs.getBigDecimal("totalServicos"));
                estatistica.somarIss(rs.getBigDecimal("totalPagar"));
            }
            return null;
        });
        return estatistica;
    }

    public Page<String> buscarDiscriminacoesPorPrestador(Pageable pageable, Long prestador) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_prestador", prestador);
        parameters.addValue("offset", pageable.getOffset());
        parameters.addValue("limit", pageable.getPageSize());

        String select = " select " +
                "     descriminacaoservico as value " +
                "   from (" +
                "  select distinct nf.descriminacaoservico " +
                "    from notafiscal nf " +
                "   inner join declaracaoprestacaoservico dec on dec.id = nf.declaracaoprestacaoservico_id " +
                " where nf.prestador_id = :id_prestador) discriminacoes ";
        List<String> discriminacoes = namedParameterJdbcTemplate.query(select +
                " offset :offset rows fetch next :limit rows only ", parameters, new StringMapper());

        Integer count = namedParameterJdbcTemplate.query(" select count(1) as value from (" + select + ") dados ",
                parameters, new IntegerMapper()).stream().findFirst().get();

        return new PageImpl(discriminacoes, pageable, count);
    }

    public Long buscarSequencialCartaCorrecao(Long idNotaFiscal) {
        List<Long> query = jdbcTemplate.query(" select coalesce(max(sequencialcartacorrecao), 0) + 1 as value " +
                "   from cartacorrecaonfse " +
                " where notafiscal_id = :nota ", new Object[]{idNotaFiscal}, new LongMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 1L;
    }

    public void salvarCartaCorrecao(CartaCorrecaoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        dto.setSequencialCartaCorrecao(buscarSequencialCartaCorrecao(dto.getIdNotaFiscal()));
        jdbcTemplate.batchUpdate(" insert into cartacorrecaonfse (id, dataemissao, notafiscal_id, " +
                " sequencialcartacorrecao, codigoverificacao, descricaoalteracao, " +
                " tomadorserviconfse_id, prestador_id) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?) ", dto);
    }

    public Page<CartaCorrecaoNfseDTO> buscarCartaCorrecaoPorNotaFiscal(Pageable pageable, Long idNotaFiscal) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_nota", idNotaFiscal);
        parameters.addValue("offset", pageable.getOffset());
        parameters.addValue("limit", pageable.getPageSize());

        String select = " select " +
                "    id, dataemissao, notafiscal_id, " +
                "    sequencialcartacorrecao, codigoverificacao, descricaoalteracao, " +
                "    tomadorserviconfse_id, prestador_id " +
                "   from cartacorrecaonfse " +
                " where notafiscal_id = :id_nota ";

        List<CartaCorrecaoNfseDTO> dados = namedParameterJdbcTemplate.query(select +
                " offset :offset rows fetch first :limit rows only ", parameters, new CartaCorrecaoNfseDTO());

        Integer count = namedParameterJdbcTemplate.query(" select count(1) as value from (" + select + ") dados ",
                parameters, new IntegerMapper()).stream().findFirst().get();

        return new PageImpl(dados, pageable, count);
    }

    public Long buscarQuantidadeNotasFiscaisEmitidas() {
        String sql = " select count(1) as value " +
                "    from declaracaoprestacaoservico dec " +
                "   inner join notafiscal nf on nf.DECLARACAOPRESTACAOSERVICO_ID = dec.ID " +
                " where dec.situacao != 'CANCELADA'";
        List<Long> query = jdbcTemplate.query(sql, new LongMapper());
        if (query != null) {
            return query.get(0);
        }
        return 0L;
    }

    public List<Long> buscarIdDoCalculoPorNotaFiscal(Long idNotaFiscal, Boolean issRetido) {
        String sql = "select calculo.id " +
                "  from declaracaoprestacaoservico dec  " +
                " inner join itemdeclaracaoservico item on dec.id = item.declaracaoprestacaoservico_id  " +
                " inner join notafiscal nf on dec.id = nf.declaracaoprestacaoservico_id  " +
                " inner join notadeclarada notadec on notadec.declaracaoprestacaoservico_id = dec.id " +
                " inner join declaracaomensalservico dms on dms.id = notadec.declaracaomensalservico_id " +
                " inner join processocalculo proc on proc.id = dms.processocalculo_id " +
                " inner join calculo on calculo.processocalculo_id = proc.id " +
                " where nf.id = :idnota ";
        if (issRetido) {
            sql += " and dms.tipomovimento in (:retencao, :iss_retido) ";
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idnota", idNotaFiscal);
        if (issRetido) {
            parameters.addValue("retencao", TipoMovimentoMensalNfseDTO.RETENCAO.name());
            parameters.addValue("iss_retido", TipoMovimentoMensalNfseDTO.ISS_RETIDO.name());
        }

        return namedParameterJdbcTemplate.queryForList(sql, parameters, Long.class);
    }

    public String getNumeroDAM(Long id, Boolean issRetido) {
        String sql = String.format(" select func_numerodam_declaracao(%1$s, %2$s) as value from dual ", id,
                (issRetido ? 1 : 0));
        return namedParameterJdbcTemplate.queryForObject(sql,
                new HashMap<>(), new StringMapper());
    }
}
