package br.com.webpublico.repository;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.enumeration.Mes;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.LongMapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DeclaracaoMensalServicoJDBCRepository implements Serializable {

    private static final String SELECT = " select " +
            " dms.id, ce.id as prestador_id, ce.inscricaocadastral, " +
            " coalesce(pf.nome, pj.razaosocial) as nome,  coalesce(pf.cpf, pj.cnpj) as cpf_cnpj, " +
            " dms.codigo, dms.tipo, dms.mes, dms.situacao, dms.qtdnotas, " +
            " dms.totalservicos, dms.totaliss, e.ano, dms.abertura, dms.encerramento, " +
            " dms.tipomovimento, dms.datacancelamento, uc.id as id_usuariocancelamento, dms.arquivodesif_id, " +
            " uc.login as login_usuariocancelamento, pvd.vencimento, c.id as calculo_id ";
    private static final String FROM = "   from declaracaomensalservico dms " +
            "  inner join cadastroeconomico ce on ce.id = dms.prestador_id " +
            "  left join usuarioweb uc on uc.id = dms.usuariocancelamentoweb_id " +
            "  inner join pessoa p on p.id = ce.pessoa_id " +
            "  left join pessoafisica pf on pf.id = p.id " +
            "  left join pessoajuridica pj on pj.id = p.id " +
            "  inner join exercicio e on e.id = dms.exercicio_id " +
            "  left join processocalculo pc on pc.id = dms.processocalculo_id " +
            "  left join calculo c on c.processocalculo_id = pc.id " +
            "  left join valordivida vd on vd.calculo_id = c.id " +
            "  left join parcelavalordivida pvd on pvd.valordivida_id = vd.id " +
            "  left join situacaoparcelavalordivida spvd on spvd.id = pvd.situacaoatual_id ";
    private static final String SQL_RELATORIO_DMS = " select ce.id as cmc_id, " +
            "       ce.inscricaocadastral as cmc_inscricao, " +
            "       coalesce(ce_pf.cpf, ce_pj.cnpj) as cmc_identificacao, " +
            "       coalesce(ce_pf.nome, ce_pj.razaosocial) as cmc_nome, " +
            "       ce_pj.nomefantasia as cmc_fantasia, " +
            "       ce.classificacaoatividade  as cmc_classificacao, " +
            "       dms.mes as dms_mes, " +
            "       e.ano as dms_ano, " +
            "       dms.tipo as dms_tipo, " +
            "       dms.situacao as dms_situacao, " +
            "       dms.totalservicos as dms_total_servicos, " +
            "       dms.totaliss as dms_total_iss, " +
            "       dms.abertura as dms_abertura, " +
            "       dms.encerramento as dms_encerramento, " +
            "       dms.id as dms_id, " +
            "       dms.tipomovimento as dms_tipo_movimento, " +
            "       case " +
            "          when dms.tipomovimento = 'NORMAL' then coalesce(dp_tomador.cpfcnpj, '') " +
            "          when dms.tipomovimento = 'RETENCAO' then coalesce(dp_prestador.cpfcnpj, '') " +
            "       end as destinado_cpf, " +
            "       case " +
            "          when dms.tipomovimento = 'NORMAL' then coalesce(dp_tomador.nomerazaosocial,'') " +
            "          when dms.tipomovimento = 'RETENCAO' then coalesce(dp_prestador.nomerazaosocial,'') " +
            "       end as destinado_nome, " +
            "       dps.totalservicos   as doc_total_servicos, " +
            "       case " +
            "          when dms.tipomovimento = 'NORMAL' then dps.isspagar " +
            "          when dms.tipomovimento = 'RETENCAO' then dps.isscalculado " +
            "          when dms.tipomovimento = 'INSTITUICAO_FINANCEIRA' then dps.isspagar " +
            "       end as doc_iss_pagar, " +
            "       coalesce(nf.emissao, sd.emissao) as doc_emissao, " +
            "       dps.situacao as doc_situacao, " +
            "       coalesce(dps.issretido, 0) as doc_retido, " +
            "       dps.naturezaoperacao as doc_natureza_op, " +
            "       coalesce(nf.numero, sd.numero) as doc_numero, " +
            "       calculo.id as calculo_id, " +
            "       pgcc.conta as if_conta, " +
            "       pgcc.nome as if_descricao, " +
            "       ids.saldoanterior as if_saldo_anterior, " +
            "       ids.credito as if_credito, " +
            "       ids.debito as if_debito, " +
            "       ids.valorservico as if_valor_declarado," +
            "       ce_pj.cnpj as cnpjPrestador " +
            "   from declaracaomensalservico dms " +
            "  inner join exercicio e on dms.exercicio_id = e.id " +
            "  inner join cadastroeconomico ce on ce.id = dms.prestador_id " +
            "  left join pessoafisica ce_pf on ce_pf.id = ce.pessoa_id " +
            "  left join pessoajuridica ce_pj on ce_pj.id = ce.pessoa_id " +
            "  inner join enquadramentofiscal enquadramento on enquadramento.cadastroeconomico_id = ce.id " +
            "                                              and enquadramento.fimvigencia is null " +
            "  inner join notadeclarada nd on nd.declaracaomensalservico_id = dms.id " +
            "  inner join declaracaoprestacaoservico dps on dps.id = nd.declaracaoprestacaoservico_id " +
            "  left join dadospessoaisnfse dp_tomador on dp_tomador.id = dps.dadospessoaistomador_id " +
            "  left join dadospessoaisnfse dp_prestador on dp_prestador.id = dps.dadospessoaisprestador_id " +
            "  left join notafiscal nf on nf.declaracaoprestacaoservico_id = dps.id " +
            "  left join servicodeclarado sd on sd.declaracaoprestacaoservico_id = dps.id " +
            "  left join calculo on calculo.processocalculo_id = dms.processocalculo_id " +
            "  left join itemdeclaracaoservico ids on ids.declaracaoprestacaoservico_id = dps.id and dms.tipomovimento = 'INSTITUICAO_FINANCEIRA' " +
            "  left join pgcc on pgcc.id = ids.conta_id ";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NotaFiscalJDBCRepository notaFiscalJDBCRepository;
    @Autowired
    IdJDBCRepository idJDBCRepository;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public DeclaracaoMensalServicoNfseDTO findByCalculoId(Long calculoId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id_calculo", calculoId);
        List<DeclaracaoMensalServicoNfseDTO> query = namedParameterJdbcTemplate.query(
                SELECT +
                        FROM +
                        "   left join inscricaodividaparcela idp on idp.parcelavalordivida_id = pvd.id " +
                        "  where c.id = :id_calculo or idp.iteminscricaodividaativa_id = :id_calculo  ",
                map,
                new DeclaracaoMensalServicoNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public boolean hasDeclaracaoMensalServico(DeclaracaoPrestacaoServicoNfseDTO declaracao) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("DEC_ID", declaracao.getId());
        if (declaracao.getIssRetido()) {
            parameters.addValue("TIPO_MOVIMENTO", TipoMovimentoMensalNfseDTO.RETENCAO.name());
        } else {
            parameters.addValue("TIPO_MOVIMENTO", TipoMovimentoMensalNfseDTO.NORMAL.name());
        }
        parameters.addValue("SITUACAO", SituacaoDeclaracaoMensalServicoNfseDTO.CANCELADO.name());
        List<Integer> query = namedParameterJdbcTemplate.query(" SELECT 1 AS VALUE FROM NOTADECLARADA ND " +
                        " INNER JOIN DECLARACAOMENSALSERVICO DMS ON DMS.ID = ND.DECLARACAOMENSALSERVICO_ID " +
                        " WHERE ND.DECLARACAOPRESTACAOSERVICO_ID = :DEC_ID" +
                        "   AND DMS.TIPOMOVIMENTO = :TIPO_MOVIMENTO" +
                        "   AND DMS.SITUACAO != :SITUACAO  ", parameters,
                new IntegerMapper());
        return query != null && !query.isEmpty();
    }

    public boolean hasDeclaracaoMensalServico(DeclaracaoPrestacaoServicoNfseDTO declaracao, TipoMovimentoMensalNfseDTO tipoMovimento) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("DEC_ID", declaracao.getId());
        parameters.addValue("TIPO_MOVIMENTO", tipoMovimento.name());
        parameters.addValue("SITUACAO", SituacaoDeclaracaoMensalServicoNfseDTO.CANCELADO.name());
        List<Integer> query = namedParameterJdbcTemplate.query(" SELECT 1 AS VALUE FROM NOTADECLARADA ND " +
                        " INNER JOIN DECLARACAOMENSALSERVICO DMS ON DMS.ID = ND.DECLARACAOMENSALSERVICO_ID " +
                        " WHERE ND.DECLARACAOPRESTACAOSERVICO_ID = :DEC_ID" +
                        "   AND DMS.TIPOMOVIMENTO = :TIPO_MOVIMENTO" +
                        "   AND DMS.SITUACAO != :SITUACAO  ", parameters,
                new IntegerMapper());
        return query != null && !query.isEmpty();
    }


    public EstatisticaMensalNfseDTO buscarServicosPrestadosNaReferencia(Integer mes, Integer ano, Long cadastroId) {

        String sql = "select extract(month from dps.COMPETENCIA) as mes, " +
                "       extract(YEAR from dps.COMPETENCIA) as ano," +
                "       coalesce(dps.ISSRETIDO,0) as retido, " +
                "       dps.SITUACAO as situacao, " +
                "       count(dps.id) as count_id, " +
                "       sum(coalesce(dps.TOTALSERVICOS, 0)) as totalServicos, " +
                "       sum(coalesce(dps.ISSCALCULADO, 0)) as totalPagar " +
                "  from DECLARACAOPRESTACAOSERVICO dps " +
                " left join NOTAFISCAL nota on nota.declaracaoprestacaoservico_id = dps.id " +
                " left join servicodeclarado sd on sd.declaracaoprestacaoservico_id = dps.id " +
                " inner join CADASTROECONOMICO cmc on cmc.id = coalesce(nota.prestador_id, sd.cadastroeconomico_id) " +
                " where cmc.ID = :cadastroId " +
                " and extract(year from dps.competencia) = :ano" +
                " and extract(month from dps.competencia) = :mes" +
                " group by coalesce(dps.ISSRETIDO,0), " +
                "         dps.SITUACAO, " +
                "         dps.TIPODOCUMENTO, " +
                "         extract(month from dps.COMPETENCIA), extract(year from dps.COMPETENCIA), extract(YEAR from dps.COMPETENCIA) " +
                " order by extract(YEAR from dps.COMPETENCIA), extract(year from dps.COMPETENCIA), extract(month from dps.COMPETENCIA) ";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cadastroId", cadastroId);
        parameters.addValue("mes", mes);
        parameters.addValue("ano", ano);
        EstatisticaMensalNfseDTO estatistica = new EstatisticaMensalNfseDTO(mes, ano);

        namedParameterJdbcTemplate.query(sql, parameters, (ResultSetExtractor<EstatisticaMensalNfseDTO>) rs -> {
            while (rs.next()) {
                boolean retido = rs.getBoolean("retido");
                String situacao = rs.getString("situacao");
                int quantidade = rs.getInt("count_id");

                if (retido) {
                    estatistica.somarRetidas(quantidade);
                }
                if (SituacaoDeclaracaoNfseDTO.CANCELADA.name().equalsIgnoreCase(situacao)) {
                    estatistica.somarCanceladas(quantidade);
                }
                estatistica.somarEmitidas(quantidade);
                estatistica.somarServico(rs.getBigDecimal("totalServicos"));
                estatistica.somarIss(rs.getBigDecimal("totalPagar"));
            }
            return null;
        });
        return estatistica;
    }

    public Integer buscarUltimoNumeroDeDeclaracaoDoPrestador(Long prestadorId) {
        List<Integer> query = jdbcTemplate.query(" select coalesce(max(a.codigo), 0) as value from declaracaomensalservico a " +
                        " where a.prestador_id = ? ",
                new Object[]{prestadorId}, new IntegerMapper());
        if (query != null && query.stream().findFirst().isPresent()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public boolean hasDeclaracaoNaCompetencia(Long prestadorId,
                                              Mes mes,
                                              Integer exercicio,
                                              TipoMovimentoMensalNfseDTO tipoMovimento) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("empresaId", prestadorId);
        parameters.addValue("mes", mes.name());
        parameters.addValue("exercicio", exercicio);
        parameters.addValue("tipoMovimento", tipoMovimento.name());
        parameters.addValue("situacao", SituacaoDeclaracaoMensalServicoNfseDTO.ENCERRADO.name());
        List<Long> query = namedParameterJdbcTemplate.query("select a.id as value " +
                        "   from DeclaracaoMensalServico a " +
                        " inner join exercicio ex on ex.id = a.exercicio_id " +
                        " where a.prestador_id =:empresaId " +
                        "   and a.mes = :mes " +
                        "   and ex.ano = :exercicio" +
                        "   and a.tipoMovimento = :tipoMovimento " +
                        "   and a.situacao = :situacao ", parameters,
                new LongMapper());
        return query != null && !query.isEmpty();
    }

    public void inserir(DeclaracaoMensalServicoNfseDTO declaracao) {
        declaracao.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into declaracaomensalservico (id, codigo, tipo, prestador_id, " +
                        "                                     mes, exercicio_id, situacao, qtdnotas, totalservicos, " +
                        "                                     totaliss, abertura, encerramento, tipomovimento, referencia, " +
                        "                                     usuariodeclaracao, arquivodesif_id) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                declaracao);

        if (declaracao.getNotasDeclaradas() != null && !declaracao.getNotasDeclaradas().isEmpty()) {
            for (NotaDeclaradaNfseDTO notaDeclarada : declaracao.getNotasDeclaradas()) {
                notaDeclarada.setId(idJDBCRepository.getId());
                notaDeclarada.setIdDeclaracaoMensalServico(declaracao.getId());
                jdbcTemplate.batchUpdate(" insert into notadeclarada (id, declaracaomensalservico_id, declaracaoprestacaoservico_id) " +
                                " values (?, ?, ?) ",
                        notaDeclarada);
            }
        }
    }

    public Long contarCalculoIss(Long idCadastro, Long idPessoa, Integer ano, Integer mes) {
        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("ano", ano);
        parametros.addValue("mes", mes);
        if (idCadastro != null) {
            parametros.addValue("id_cadastro", idCadastro);
        } else {
            parametros.addValue("id_pessoa", idPessoa);
        }

        String sql = " select count(distinct c.id) as value " +
                "   from processocalculoiss pc " +
                " inner join processocalculo p on p.id = pc.id " +
                " inner join exercicio e on e.id = p.exercicio_id " +
                " inner join calculo c on c.processocalculo_id = pc.id " +
                " inner join calculopessoa cp on cp.calculo_id = c.id " +
                " where e.ano = :ano " +
                "   and pc.mesreferencia = :mes ";
        if (idCadastro != null) {
            sql += " and c.cadastro_id = :id_cadastro ";
        } else {
            sql += " and cp.pessoa_id = :id_pessoa ";
        }

        List<Long> query = namedParameterJdbcTemplate.query(sql,
                parametros,
                new LongMapper());
        if (query != null && query.stream().findFirst().isPresent())
            return query.stream().findFirst().get();
        return 0L;
    }

    public List<ServicoDeclaracaoMensalNfseDTO> buscarServicosDeclaracaoMensal(Long idDeclaracao) {
        return jdbcTemplate.query(
                " select servico.id as id_servico, " +
                        "        servico.aliquotaisshomologado as aliquota, " +
                        "        sum(item.basecalculo) as base_calculo, " +
                        "        sum(item.valorservico) as total_servicos," +
                        "        sum(case " +
                        "               when dms.tipomovimento = 'RETENCAO' or dec.isspagar > 0 then item.iss" +
                        "               else 0 " +
                        "            end) as iss   " +
                        "    from itemdeclaracaoservico item " +
                        "   inner join servico on servico.id = item.servico_id " +
                        "   inner join declaracaoprestacaoservico dec on dec.id = item.declaracaoprestacaoservico_id " +
                        "   inner join notadeclarada nd on nd.declaracaoprestacaoservico_id = dec.id " +
                        "   inner join declaracaomensalservico dms on dms.id = nd.declaracaomensalservico_id " +
                        " where dms.id = ? " +
                        " group by servico.id, servico.aliquotaisshomologado  ",
                new Object[]{idDeclaracao},
                new ServicoDeclaracaoMensalNfseDTO());
    }

    public void vincularProcessoCalculo(Long idDeclaracao, Long idProcessoCalculo) {
        jdbcTemplate.batchUpdate(" update declaracaomensalservico set processocalculo_id = ? " +
                " where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, idProcessoCalculo);
                preparedStatement.setLong(2, idDeclaracao);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public List<DeclaracaoMensalServicoNfseDTO> consultarDeclaracaoMensalServico(Pageable pageable,
                                                                                 List<ParametroQuery> parametros,
                                                                                 String orderBy) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        if (Strings.isNullOrEmpty(orderBy)) {
            orderBy = " order by dms.id desc ";
        }
        sql.append(orderBy);
        parameters.addValue("offset", pageable.getOffset());
        parameters.addValue("limit", pageable.getPageSize());
        sql.append(" offset :offset rows fetch first :limit rows only ");

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new DeclaracaoMensalServicoNfseDTO());
    }

    public Integer contarDeclaracaoMensalServico(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(dms.id) as value ").append(FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public DeclaracaoMensalServicoNfseDTO findById(Long id) {
        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("id", id);
        List<DeclaracaoMensalServicoNfseDTO> query = namedParameterJdbcTemplate.query(
                SELECT +
                        FROM + " where dms.id = :id ",
                parametros, new DeclaracaoMensalServicoNfseDTO());
        if (query != null && query.stream().findFirst().isPresent())
            return query.stream().findFirst().get();
        return null;
    }

    public List<NotaDeclaradaNfseDTO> buscarNotasDeclaradas(Long idDms) {
        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("id_dms", idDms);
        return namedParameterJdbcTemplate.query(" select " +
                        " id, declaracaomensalservico_id, declaracaoprestacaoservico_id " +
                        "  from notadeclarada " +
                        " where declaracaomensalservico_id = :id_dms ",
                parametros, new NotaDeclaradaNfseDTO());
    }

    public void cancelar(DeclaracaoMensalServicoNfseDTO declaracao) {
        jdbcTemplate.batchUpdate(" update declaracaomensalservico set datacancelamento = ?," +
                " usuariocancelamentoweb_id = ?, situacao = ? where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setDate(1, DateUtils.toSQLDate(declaracao.getDataCancelamento()));
                ps.setLong(2, declaracao.getUsuarioCancelamento().getId());
                ps.setString(3, declaracao.getSituacao().name());
                ps.setLong(4, declaracao.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public List<RelatorioDeclaracaoMensalServicosNfseDTO> buscarDadosDoRelatorioDeclaracaoMensalServico(Long idDms) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idDms", idDms);

        String sql = SQL_RELATORIO_DMS +
                " where dms.id = :idDms " +
                " order by ce.inscricaocadastral, dms.id ";
        return namedParameterJdbcTemplate.query(sql, parameters, new RelatorioDeclaracaoMensalServicosNfseDTO());
    }

    public List<RelatorioDeclaracaoMensalServicosNfseDTO> buscarDadosDoRelatorioDeclaracaoMensalServico(Long idPrestador,
                                                                                                        Integer ano,
                                                                                                        Integer mes,
                                                                                                        String tipoMovimento) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idPrestador", idPrestador);
        parameters.addValue("ano", ano);
        parameters.addValue("mes", Mes.getMesToInt(mes).name());
        parameters.addValue("tipoMovimento", tipoMovimento);

        String sql = SQL_RELATORIO_DMS +
                " where ce.id = :idPrestador " +
                "   and dms.mes = :mes " +
                "   and e.ano = :ano " +
                "   and dms.tipomovimento = :tipoMovimento " +
                " order by ce.inscricaocadastral, dms.id ";
        return namedParameterJdbcTemplate.query(sql, parameters, new RelatorioDeclaracaoMensalServicosNfseDTO());
    }

    public GuiaNfseDTO buscarGuiaNfse(Long idDam) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ID_DAM", idDam);

        List<GuiaNfseDTO> query = namedParameterJdbcTemplate.query(" SELECT DISTINCT " +
                "       D.NUMERODAM AS NUMERO_DAM, " +
                "       SPVD.REFERENCIA AS REFERENCIA_DEBITO, " +
                "       PVD.VENCIMENTO AS VENCIMENTO_DEBITO, " +
                "       D.VENCIMENTO AS VENCIMENTO_DAM, " +
                "       D.EMISSAO  AS EMISSAO_DAM, " +
                "       COALESCE(D.VALORORIGINAL, 0) + COALESCE(D.JUROS, 0) +  " +
                "       COALESCE(D.MULTA, 0) + COALESCE(D.CORRECAOMONETARIA , 0) AS VALOR_DAM, " +
                "       D.SITUACAO AS SITUACAO_DAM " +
                "   FROM DAM D " +
                "  INNER JOIN ITEMDAM ID ON ID.DAM_ID = D.ID  " +
                "  INNER JOIN PARCELAVALORDIVIDA PVD ON PVD.ID = ID.PARCELA_ID  " +
                "  INNER JOIN SITUACAOPARCELAVALORDIVIDA SPVD ON SPVD.ID = PVD.SITUACAOATUAL_ID " +
                "  INNER JOIN VALORDIVIDA VD ON VD.ID = PVD.VALORDIVIDA_ID  " +
                "  INNER JOIN CALCULO C ON C.ID = VD.CALCULO_ID  " +
                "  INNER JOIN PROCESSOCALCULO PC ON PC.ID = C.PROCESSOCALCULO_ID  " +
                "WHERE D.ID = :ID_DAM ", parameters, new GuiaNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().orElse(null);
        }
        return null;
    }

    public List<NotaFiscalGuiaNfseDTO> buscarNotasFiscaisGuiaNfse(Long idDam) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ID_DAM", idDam);

        return namedParameterJdbcTemplate.query(" SELECT COALESCE(NF.NUMERO, SD.NUMERO) AS NUMERO_DOCUMENTO, " +
                "       CASE WHEN NF.ID IS NOT NULL THEN 'Nfs-e' ELSE 'Declarado' END TIPO_DOCUMENTO, " +
                "       COALESCE(NF.EMISSAO, SD.EMISSAO) AS EMISSAO, " +
                "       CASE   " +
                "          WHEN DMS.TIPOMOVIMENTO = 'NORMAL' THEN DPT.CPFCNPJ  " +
                "          WHEN DMS.TIPOMOVIMENTO = 'RETENCAO' THEN DPP.CPFCNPJ " +
                "          WHEN DMS.TIPOMOVIMENTO = 'ISS_RETIDO' THEN DPP.CPFCNPJ " +
                "       END CPF_CNPJ, " +
                "       CASE   " +
                "          WHEN DMS.TIPOMOVIMENTO = 'NORMAL' THEN DPT.NOMERAZAOSOCIAL  " +
                "          WHEN DMS.TIPOMOVIMENTO = 'RETENCAO' THEN DPP.NOMERAZAOSOCIAL  " +
                "          WHEN DMS.TIPOMOVIMENTO = 'ISS_RETIDO' THEN DPP.NOMERAZAOSOCIAL  " +
                "       END NOME_RAZAOSOCIAL, " +
                "       DEC.TOTALSERVICOS AS TOTAL_SERVICOS, " +
                "       DEC.ISSCALCULADO AS ISS " +
                "   FROM DECLARACAOPRESTACAOSERVICO DEC " +
                "  INNER JOIN DADOSPESSOAISNFSE DPP ON DPP.ID = DEC.DADOSPESSOAISPRESTADOR_ID  " +
                "  LEFT JOIN DADOSPESSOAISNFSE DPT ON DPT.ID = DEC.DADOSPESSOAISTOMADOR_ID  " +
                "  LEFT JOIN NOTAFISCAL NF ON NF.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                "  LEFT JOIN SERVICODECLARADO SD ON SD.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID  " +
                "  INNER JOIN NOTADECLARADA ND ON ND.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                "  INNER JOIN DECLARACAOMENSALSERVICO DMS ON DMS.ID = ND.DECLARACAOMENSALSERVICO_ID  " +
                "  INNER JOIN PROCESSOCALCULO PC ON DMS.PROCESSOCALCULO_ID = PC.ID  " +
                "  INNER JOIN CALCULO C ON C.PROCESSOCALCULO_ID = PC.ID " +
                "  INNER JOIN VALORDIVIDA VD ON VD.CALCULO_ID = C.ID  " +
                "  INNER JOIN PARCELAVALORDIVIDA PVD ON PVD.VALORDIVIDA_ID = VD.ID " +
                "  INNER JOIN ITEMDAM ID ON ID.PARCELA_ID = PVD.ID  " +
                "  INNER JOIN DAM D ON D.ID = ID.DAM_ID  " +
                "WHERE D.ID = :ID_DAM " +
                "ORDER BY COALESCE(NF.EMISSAO, SD.EMISSAO) ", parameters, new NotaFiscalGuiaNfseDTO());
    }

    public Long buscarIdCalculoDeclaracao(Long idDeclaracao) {
        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("idDeclaracao", idDeclaracao);

        String sql = " select c.id as value " +
                "   from declaracaomensalservico dms " +
                " left join processocalculo pc on pc.id = dms.processocalculo_id " +
                " inner join calculo c on c.processocalculo_id = pc.id " +
                " where dms.id = :idDeclaracao ";

        return namedParameterJdbcTemplate.queryForObject(sql, parametros, new LongMapper());
    }
}
