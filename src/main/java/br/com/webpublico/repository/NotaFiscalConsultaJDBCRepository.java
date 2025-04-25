package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ResumoDMSDTO;
import br.com.webpublico.domain.dto.ResumoNaturezaDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.webreportdto.dto.tributario.SituacaoNotaDTO;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NotaFiscalConsultaJDBCRepository extends AbstractJDBCRepository<NotaFiscalSearchDTO> {

    @Override
    public String getSelect() {
        return "select " +
                "    obj.id as id_nota, " +
                "    dps.tipodocumento as tipo_documento, " +
                "    obj.prestador_id as id_prestador, " +
                "    tnf.id as id_tomador," +
                "    rps.id as id_rps," +
                "    rps.numero as numero_rps," +
                "    obj.numero as numero_nota," +
                "    obj.emissao as emissao_nota," +
                "    dptnf.nomerazaosocial as nome_tomador," +
                "    dppnf.nomerazaosocial as nome_prestador," +
                "    dptnf.cpfcnpj as cpfcnpj_tomador," +
                "    dppnf.cpfcnpj as cpfcnpj_prestador," +
                "    dps.situacao as situacao," +
                "    dps.modalidade as modalidade," +
                "    dps.issretido as issretido," +
                "    dps.totalservicos as totalservicos," +
                "    dps.deducoeslegais as deducoeslegais," +
                "    dps.basecalculo as basecalculo," +
                "    dps.isspagar as isspagar," +
                "    dps.isscalculado as isscalculado," +
                "    dps.naturezaoperacao as naturezaoperacao, " +
                "    obj.descriminacaoServico as descriminacaoservico," +
                "    rps.serie as serie_rps, " +
                "    rps.tipoRps as tipo_rps ";
    }

    @Override
    public String getFrom() {
        return "   from notafiscal obj " +
                "  inner join declaracaoprestacaoservico dps on dps.id = obj.declaracaoprestacaoservico_id " +
                "  left join intermediarioservico i on i.id = dps.intermediario_id " +
                "  left join rps rps on rps.id = obj.rps_id " +
                "  left join tomadorserviconfse tnf on tnf.id = obj.tomador_id " +
                "  left join dadospessoaisnfse dptnf on dptnf.id = dps.dadospessoaistomador_id " +
                "  left join dadospessoaisnfse dppnf on dppnf.id = dps.dadospessoaisprestador_id " +
                "  left join cadastroeconomico cep on cep.id = obj.prestador_id ";
    }

    @Override
    public RowMapper<NotaFiscalSearchDTO> newRowMapper() {
        return new NotaFiscalSearchDTO();
    }

    @Override
    public NotaFiscalSearchDTO insert(NotaFiscalSearchDTO dto) {
        return dto;
    }

    @Override
    public NotaFiscalSearchDTO update(NotaFiscalSearchDTO dto) {
        return dto;
    }

    @Override
    public void remove(NotaFiscalSearchDTO dto) {
    }

    public List<NotaFiscalSearchDTO> findByCalculo(Long idCalculo) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id_calculo", idCalculo);
        List<NotaFiscalSearchDTO> query = namedParameterJdbcTemplate.query(getSelect() + getFrom() +
                        " inner join notadeclarada nd on nd.declaracaoprestacaoservico_id= dps.id " +
                        " inner join declaracaomensalservico dms on dms.id = nd.declaracaomensalservico_id " +
                        " inner join processocalculo proc on proc.id = dms.processocalculo_id " +
                        " inner join calculo on calculo.processocalculo_id = proc.id " +
                        " inner join valordivida vd on vd.calculo_id = calculo.id " +
                        " inner join parcelavalordivida pvd on pvd.valordivida_id = vd.id " +
                        " inner join SITUACAOPARCELAVALORDIVIDA spvd on spvd.id = pvd.SITUACAOATUAL_ID " +
                        " left join inscricaodividaparcela idp on spvd.SITUACAOPARCELA ='INSCRITA_EM_DIVIDA_ATIVA' and idp.parcelavalordivida_id = pvd.id " +
                        " left join INSCRICAODIVIDAATIVA ida on ida.id = idp.ITEMINSCRICAODIVIDAATIVA_ID and ida.SITUACAOINSCRICAODIVIDAATIVA <> 'CANCELADO' " +
                        " where (calculo.id = :id_calculo or idp.iteminscricaodividaativa_id = :id_calculo) " +
                        "   and ((dms.tipoMovimento = 'NORMAL' and dps.isspagar > 0) or (dms.tipoMovimento != 'NORMAL') ) ",
                map,
                new NotaFiscalSearchDTO());
        if (query != null && !query.isEmpty()) {
            return query;
        }
        return null;
    }

    public List<NotaFiscalSearchDTO> buscarNotasPorCompetenciaAndTipo(Long prestadorId, Integer exercicio, Integer
            mes, TipoMovimentoMensalNfseDTO tipoMovimentoMensal, boolean declarada, boolean issDevido, int page, int size) {
        switch (tipoMovimentoMensal) {
            case NORMAL: {
                return buscarNotasPorCompetenciaServicosPrestados(prestadorId, exercicio, mes, declarada, issDevido, page, size);
            }
            case RETENCAO: {
                return buscarNotasPorCompetenciaServicosTomados(prestadorId, exercicio, mes, declarada, page, size);
            }
            case ISS_RETIDO:
                return buscarNotasPrestadasIssRetidoSemDeclararPorCompetencia(prestadorId, mes, exercicio, page, size);
        }
        return Lists.newArrayList();
    }

    public List<NotaFiscalSearchDTO> buscarNotasPrestadasIssRetidoSemDeclararPorCompetencia(Long prestadorId,
                                                                                            int mes, int exercicio,
                                                                                            int page, int size) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("PRESTADOR_ID", prestadorId);
        parameters.addValue("EXERCICIO", exercicio);
        parameters.addValue("MES", mes);
        parameters.addValue("CANCELADA", SituacaoDeclaracaoNfseDTO.CANCELADA.name());
        parameters.addValue("TIPO_SERVICO", TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.name());

        String sql = "  SELECT  " +
                "   DEC.ID, " +
                "   DEC.TIPODOCUMENTO AS TIPO,  " +
                "   COALESCE(NOTA.NUMERO, SDP.NUMERO) AS NUMERO,  " +
                "   COALESCE(NOTA.EMISSAO, SDP.EMISSAO) AS EMISSAO,  " +
                "   DPP.NOMERAZAOSOCIAL AS PRESTADOR_NOMERAZAOSOCIAL,  " +
                "   DPP.CPFCNPJ AS PRESTADOR_CPFCNPJ,  " +
                "   DPT.NOMERAZAOSOCIAL AS TOMADOR_NOMERAZAOSOCIAL,  " +
                "   DPT.CPFCNPJ AS TOMADOR_CPFCNPJ,  " +
                "   DEC.SITUACAO, " +
                "   COALESCE(DEC.TOTALSERVICOS, 0) AS TOTAL_SERVICOS,  " +
                "   COALESCE(COALESCE(DEC.DESCONTOSINCONDICIONAIS, DEC.DESCONTOSCONDICIONAIS), 0) AS DESCONTO, " +
                "   COALESCE(DEC.DEDUCOESLEGAIS, 0) AS DEDUCOES, " +
                "   COALESCE(DEC.VALORLIQUIDO, 0) AS VALORLIQUIDO,  " +
                "   COALESCE(DEC.BASECALCULO, 0) AS BASE_CALCULO,  " +
                "   COALESCE(IDS.ALIQUOTASERVICO, 0) AS ALIQUOTA,  " +
                "   COALESCE(DEC.ISSRETIDO, 0) AS ISS_RETIDO,  " +
                "   COALESCE(DEC.ISSCALCULADO, 0) AS ISS_CALCULADO,  " +
                "   COALESCE(DEC.ISSPAGAR, 0) AS ISS_PAGAR,  " +
                "   COALESCE(NOTA.PRESTADOR_ID, SDP.CADASTROECONOMICO_ID) AS ID_PRESTADOR, " +
                "   DEC.NATUREZAOPERACAO  " +
                "  FROM DECLARACAOPRESTACAOSERVICO DEC  " +
                " INNER JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                " LEFT JOIN NOTAFISCAL NOTA ON NOTA.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID  " +
                " LEFT JOIN SERVICODECLARADO SDP ON SDP.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID AND SDP.TIPOSERVICODECLARADO = :TIPO_SERVICO " +
                " LEFT JOIN DADOSPESSOAISNFSE DPP ON DPP.ID = DEC.DADOSPESSOAISPRESTADOR_ID  " +
                " LEFT JOIN DADOSPESSOAISNFSE DPT ON DPT.ID = DEC.DADOSPESSOAISTOMADOR_ID " +
                " INNER JOIN CADASTROECONOMICO CE ON ce.id =  coalesce(nota.prestador_id, sdp.cadastroeconomico_id) " +
                "WHERE CE.ID =:PRESTADOR_ID  " +
                "   AND COALESCE(NOTA.HOMOLOGACAO, 0) = 0 " +
                "   AND DEC.SITUACAO != :CANCELADA " +
                "   AND DEC.ISSRETIDO = 1 " +
                "   AND EXTRACT(YEAR FROM DEC.COMPETENCIA) = :EXERCICIO " +
                "   AND EXTRACT(MONTH FROM DEC.COMPETENCIA) = :MES ";

        sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.RETENCAO.name() + "', DEC.ID) = 0 ";
        sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 0 ";

        sql += " ORDER BY DEC.TIPODOCUMENTO, COALESCE(NOTA.NUMERO, SDP.NUMERO) OFFSET " + (page * size) + " ROWS FETCH NEXT " + size + " ROWS ONLY ";


        return namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> popularNotaFiscalSearchDTO(resultSet));
    }

    private static NotaFiscalSearchDTO popularNotaFiscalSearchDTO(ResultSet resultSet) throws SQLException {
        NotaFiscalSearchDTO dto = new NotaFiscalSearchDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setTipoDocumentoNfse(resultSet.getString("TIPO"));
        dto.setNumero(resultSet.getLong("NUMERO"));
        dto.setEmissao(resultSet.getDate("EMISSAO"));
        dto.setNomePrestador(resultSet.getString("PRESTADOR_NOMERAZAOSOCIAL"));
        dto.setCpfCnpjPrestador(resultSet.getString("PRESTADOR_CPFCNPJ"));
        dto.setNomeTomador(resultSet.getString("TOMADOR_NOMERAZAOSOCIAL"));
        dto.setCpfCnpjTomador(resultSet.getString("TOMADOR_CPFCNPJ"));
        dto.setSituacao(resultSet.getString("SITUACAO"));
        dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
        dto.setDesconto(resultSet.getBigDecimal("DESCONTO"));
        dto.setDeducoes(resultSet.getBigDecimal("DEDUCOES"));
        dto.setValorLiquido(resultSet.getBigDecimal("VALORLIQUIDO"));
        dto.setBaseCalculo(resultSet.getBigDecimal("BASE_CALCULO"));
        dto.setAliquota(resultSet.getBigDecimal("ALIQUOTA"));
        dto.setIssRetido(resultSet.getBoolean("ISS_RETIDO"));
        dto.setIssCalculado(resultSet.getBigDecimal("ISS_CALCULADO"));
        dto.setIss(resultSet.getBigDecimal("ISS_PAGAR"));
        dto.setIdPrestador(resultSet.getLong("ID_PRESTADOR"));
        dto.setNaturezaOperacao(resultSet.getString("NATUREZAOPERACAO"));
        return dto;
    }

    public ResumoDMSDTO buscarResumoPorCompetenciaAndTipo(Long prestadorId, Integer exercicio, Integer
            mes, TipoMovimentoMensalNfseDTO tipoMovimentoMensal, boolean declarada, boolean issDevido) {
        switch (tipoMovimentoMensal) {
            case NORMAL: {
                return buscarResumoPorCompetenciaServicosPrestados(prestadorId, exercicio, mes, declarada, issDevido);
            }
            case RETENCAO: {
                return buscarResumoPorCompetenciaServicosTomados(prestadorId, exercicio, mes, declarada);
            }
        }
        return new ResumoDMSDTO();
    }

    public List<NotaFiscalSearchDTO> buscarNotasPorCompetenciaServicosPrestados(Long prestadorId, Integer exercicio,
                                                                                Integer mes, boolean declarada,
                                                                                boolean issDevido, int page, int size) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("PRESTADOR_ID", prestadorId);
        parameters.addValue("EXERCICIO", exercicio);
        parameters.addValue("MES", mes);
        parameters.addValue("TIPO_SERVICO_PRESTADO", TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.name());
        parameters.addValue("CANCELADA", SituacaoDeclaracaoNfseDTO.CANCELADA.name());

        String sql = "  SELECT  " +
                "   DEC.ID, " +
                "   DEC.TIPODOCUMENTO AS TIPO,  " +
                "   COALESCE(NOTA.NUMERO, SDP.NUMERO) AS NUMERO,  " +
                "   COALESCE(NOTA.EMISSAO, SDP.EMISSAO) AS EMISSAO,  " +
                "   DPP.NOMERAZAOSOCIAL AS PRESTADOR_NOMERAZAOSOCIAL,  " +
                "   DPP.CPFCNPJ AS PRESTADOR_CPFCNPJ,  " +
                "   DPT.NOMERAZAOSOCIAL AS TOMADOR_NOMERAZAOSOCIAL,  " +
                "   DPT.CPFCNPJ AS TOMADOR_CPFCNPJ,  " +
                "   DEC.SITUACAO, " +
                "   COALESCE(DEC.TOTALSERVICOS, 0) AS TOTAL_SERVICOS,  " +
                "   COALESCE(COALESCE(DEC.DESCONTOSINCONDICIONAIS, DEC.DESCONTOSCONDICIONAIS), 0) AS DESCONTO, " +
                "   COALESCE(DEC.DEDUCOESLEGAIS, 0) AS DEDUCOES, " +
                "   COALESCE(DEC.VALORLIQUIDO, 0) AS VALORLIQUIDO,  " +
                "   COALESCE(DEC.BASECALCULO, 0) AS BASE_CALCULO,  " +
                "   COALESCE(IDS.ALIQUOTASERVICO, 0) AS ALIQUOTA,  " +
                "   COALESCE(DEC.ISSRETIDO, 0) AS ISS_RETIDO,  " +
                "   COALESCE(DEC.ISSCALCULADO, 0) AS ISS_CALCULADO,  " +
                "   COALESCE(DEC.ISSPAGAR, 0) AS ISS_PAGAR,  " +
                "   COALESCE(NOTA.PRESTADOR_ID, SDP.CADASTROECONOMICO_ID) AS ID_PRESTADOR, " +
                "   DEC.NATUREZAOPERACAO  " +
                "  FROM DECLARACAOPRESTACAOSERVICO DEC  " +
                " INNER JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                " LEFT JOIN NOTAFISCAL NOTA ON NOTA.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID  " +
                " LEFT JOIN SERVICODECLARADO SDP ON SDP.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID AND SDP.TIPOSERVICODECLARADO = :TIPO_SERVICO_PRESTADO " +
                " INNER JOIN CADASTROECONOMICO CE ON CE.ID = COALESCE(NOTA.PRESTADOR_ID, SDP.CADASTROECONOMICO_ID) " +
                " LEFT JOIN DADOSPESSOAISNFSE DPP ON DPP.ID = DEC.DADOSPESSOAISPRESTADOR_ID  " +
                " LEFT JOIN DADOSPESSOAISNFSE DPT ON DPT.ID = DEC.DADOSPESSOAISTOMADOR_ID  " +
                "WHERE CE.ID =:PRESTADOR_ID " +
                "   AND COALESCE(NOTA.HOMOLOGACAO, 0) = 0 " +
                "   AND DEC.SITUACAO != :CANCELADA " +
                "   AND EXTRACT(YEAR FROM DEC.COMPETENCIA) = :EXERCICIO " +
                "   AND EXTRACT(MONTH FROM DEC.COMPETENCIA) = :MES ";
        if (declarada) {
            sql += " AND (FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.NORMAL.name() + "', DEC.ID) = 1 OR " +
                    " FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 1) ";
        } else {
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.NORMAL.name() + "', DEC.ID) = 0 ";
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 0 ";
        }
        if (issDevido) {
            sql += "   AND DEC.ISSPAGAR > 0 ";
        }
        sql += "ORDER BY DEC.TIPODOCUMENTO, COALESCE(NOTA.NUMERO, SDP.NUMERO) OFFSET " + (page * size) + " ROWS FETCH NEXT " + size + " ROWS ONLY ";

        return namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> {
            NotaFiscalSearchDTO dto = new NotaFiscalSearchDTO();
            dto.setId(resultSet.getLong("ID"));
            dto.setTipoDocumentoNfse(resultSet.getString("TIPO"));
            dto.setNumero(resultSet.getLong("NUMERO"));
            dto.setEmissao(resultSet.getDate("EMISSAO"));
            dto.setNomeTomador(resultSet.getString("PRESTADOR_NOMERAZAOSOCIAL"));
            dto.setCpfCnpjTomador(resultSet.getString("PRESTADOR_CPFCNPJ"));
            dto.setNomeTomador(resultSet.getString("TOMADOR_NOMERAZAOSOCIAL"));
            dto.setCpfCnpjTomador(resultSet.getString("TOMADOR_CPFCNPJ"));
            dto.setSituacao(resultSet.getString("SITUACAO"));
            dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
            dto.setDesconto(resultSet.getBigDecimal("DESCONTO"));
            dto.setDeducoes(resultSet.getBigDecimal("DEDUCOES"));
            dto.setValorLiquido(resultSet.getBigDecimal("VALORLIQUIDO"));
            dto.setBaseCalculo(resultSet.getBigDecimal("BASE_CALCULO"));
            dto.setAliquota(resultSet.getBigDecimal("ALIQUOTA"));
            dto.setIssRetido(resultSet.getBoolean("ISS_RETIDO"));
            dto.setIssCalculado(resultSet.getBigDecimal("ISS_CALCULADO"));
            dto.setIss(resultSet.getBigDecimal("ISS_PAGAR"));
            dto.setIdPrestador(resultSet.getLong("ID_PRESTADOR"));
            dto.setNaturezaOperacao(resultSet.getString("NATUREZAOPERACAO"));
            return dto;
        });
    }


    public ResumoDMSDTO buscarResumoPorCompetenciaServicosPrestados(Long prestadorId,
                                                                    Integer exercicio,
                                                                    Integer mes,
                                                                    boolean declarada,
                                                                    boolean issDevido) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("PRESTADOR_ID", prestadorId);
        parameters.addValue("EXERCICIO", exercicio);
        parameters.addValue("MES", mes);
        parameters.addValue("TIPO_SERVICO_PRESTADO", TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.name());
        parameters.addValue("CANCELADA", SituacaoDeclaracaoNfseDTO.CANCELADA.name());

        String sql = "  SELECT  " +
                "    COUNT(DEC.ID) AS QUANTIDADE," +
                "    SUM(COALESCE(DEC.TOTALSERVICOS, 0)) AS TOTAL_SERVICOS," +
                "    SUM(COALESCE(COALESCE(DEC.DESCONTOSINCONDICIONAIS, DEC.DESCONTOSCONDICIONAIS), 0)) AS DESCONTO," +
                "    SUM(COALESCE(DEC.DEDUCOESLEGAIS, 0)) AS DEDUCOES," +
                "    SUM(COALESCE(DEC.VALORLIQUIDO, 0)) AS VALORLIQUIDO," +
                "    SUM(COALESCE(DEC.BASECALCULO, 0)) AS BASE_CALCULO," +
                "    SUM(COALESCE(IDS.ALIQUOTASERVICO, 0)) AS ALIQUOTA," +
                "    SUM(COALESCE(DEC.ISSRETIDO, 0)) AS ISS_RETIDO," +
                "    SUM(COALESCE(DEC.ISSCALCULADO, 0)) AS ISS_CALCULADO," +
                "    SUM(COALESCE(DEC.ISSPAGAR, 0)) AS ISS_PAGAR," +
                "    DEC.NATUREZAOPERACAO " +
                "  FROM DECLARACAOPRESTACAOSERVICO DEC  " +
                " INNER JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                " LEFT JOIN NOTAFISCAL NOTA ON NOTA.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID  " +
                " LEFT JOIN SERVICODECLARADO SDP ON SDP.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID AND SDP.TIPOSERVICODECLARADO = :TIPO_SERVICO_PRESTADO " +
                " INNER JOIN CADASTROECONOMICO CE ON CE.ID = COALESCE(NOTA.PRESTADOR_ID, SDP.CADASTROECONOMICO_ID) " +
                "WHERE CE.ID = :PRESTADOR_ID " +
                "   AND COALESCE(NOTA.HOMOLOGACAO, 0) = 0 " +
                "   AND DEC.SITUACAO != :CANCELADA " +
                "   AND EXTRACT(YEAR FROM DEC.COMPETENCIA) = :EXERCICIO " +
                "   AND EXTRACT(MONTH FROM DEC.COMPETENCIA) = :MES ";
        if (declarada) {
            sql += " AND (FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.NORMAL.name() + "', DEC.ID) = 1 OR " +
                    " FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 1) ";
        } else {
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.NORMAL.name() + "', DEC.ID) = 0 ";
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 0 ";
        }
        if (issDevido) {
            sql += "   AND DEC.ISSPAGAR > 0 ";
        }
        sql += " GROUP BY DEC.NATUREZAOPERACAO ";

        ResumoDMSDTO dms = new ResumoDMSDTO();

        List<ResumoNaturezaDTO> naturezas = namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> {
            ResumoNaturezaDTO dto = new ResumoNaturezaDTO();
            boolean retido = resultSet.getBoolean("ISS_RETIDO");
            dto.setTotalIssDevido(resultSet.getBigDecimal("ISS_PAGAR"));
            dto.setTotalIss(resultSet.getBigDecimal("ISS_CALCULADO"));
            if (retido) {
                dto.setTotalIssRetido(resultSet.getBigDecimal("ISS_CALCULADO"));
            }
            dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
            dto.setNatureza(resultSet.getString("NATUREZAOPERACAO"));
            dto.setQtdNotas(resultSet.getInt("QUANTIDADE"));
            return dto;
        });
        dms.addNaturezas(naturezas);
        return dms;
    }


    public ResumoDMSDTO buscarResumoPorCompetenciaServicosTomados(Long prestadorId,
                                                                  Integer exercicio,
                                                                  Integer mes,
                                                                  boolean declarada) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("PRESTADOR_ID", prestadorId);
        parameters.addValue("EXERCICIO", exercicio);
        parameters.addValue("MES", mes);
        parameters.addValue("TIPO_SERVICO_TOMADO", TipoServicoDeclaradoNfseDTO.SERVICO_TOMADO.name());
        parameters.addValue("CANCELADA", SituacaoDeclaracaoNfseDTO.CANCELADA.name());

        String sql = "  SELECT  " +
                "    COUNT(DEC.ID) AS QUANTIDADE," +
                "    SUM(COALESCE(DEC.TOTALSERVICOS, 0)) AS TOTAL_SERVICOS," +
                "    SUM(COALESCE(COALESCE(DEC.DESCONTOSINCONDICIONAIS, DEC.DESCONTOSCONDICIONAIS), 0)) AS DESCONTO," +
                "    SUM(COALESCE(DEC.DEDUCOESLEGAIS, 0)) AS DEDUCOES," +
                "    SUM(COALESCE(DEC.VALORLIQUIDO, 0)) AS VALORLIQUIDO," +
                "    SUM(COALESCE(DEC.BASECALCULO, 0)) AS BASE_CALCULO," +
                "    SUM(COALESCE(IDS.ALIQUOTASERVICO, 0)) AS ALIQUOTA," +
                "    SUM(COALESCE(DEC.ISSRETIDO, 0)) AS ISS_RETIDO," +
                "    SUM(COALESCE(DEC.ISSCALCULADO, 0)) AS ISS_CALCULADO," +
                "    SUM(COALESCE(DEC.ISSPAGAR, 0)) AS ISS_PAGAR," +
                "    DEC.NATUREZAOPERACAO " +
                "  FROM DECLARACAOPRESTACAOSERVICO DEC  " +
                " INNER JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                " LEFT JOIN NOTAFISCAL NOTA ON NOTA.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID  " +
                " LEFT JOIN SERVICODECLARADO SDP ON SDP.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID AND SDP.TIPOSERVICODECLARADO = :TIPO_SERVICO_TOMADO " +
                " LEFT JOIN DADOSPESSOAISNFSE DPP ON DPP.ID = DEC.DADOSPESSOAISPRESTADOR_ID  " +
                " LEFT JOIN DADOSPESSOAISNFSE DPT ON DPT.ID = DEC.DADOSPESSOAISTOMADOR_ID " +
                " LEFT JOIN PESSOAJURIDICA PJT ON PJT.CNPJ = DPT.CPFCNPJ " +
                " INNER JOIN CADASTROECONOMICO CE ON CE.PESSOA_ID = PJT.ID " +
                "WHERE CE.ID =:PRESTADOR_ID " +
                "   AND DEC.ISSRETIDO = 1 " +
                "   AND COALESCE(NOTA.HOMOLOGACAO, 0) = 0 " +
                "   AND DEC.SITUACAO != :CANCELADA " +
                "   AND EXTRACT(YEAR FROM DEC.COMPETENCIA) = :EXERCICIO " +
                "   AND EXTRACT(MONTH FROM DEC.COMPETENCIA) = :MES ";


        if (declarada) {
            sql += " AND (FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.RETENCAO.name() + "', DEC.ID) = 1 OR " +
                    " FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 1) ";
        } else {
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.RETENCAO.name() + "', DEC.ID) = 0 ";
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 0 ";
        }
        sql += " GROUP BY DEC.NATUREZAOPERACAO ";

        ResumoDMSDTO dms = new ResumoDMSDTO();

        List<ResumoNaturezaDTO> naturezas = namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> {
            ResumoNaturezaDTO dto = new ResumoNaturezaDTO();
            boolean retido = resultSet.getBoolean("ISS_RETIDO");
            dto.setTotalIssDevido(resultSet.getBigDecimal("ISS_PAGAR"));
            dto.setTotalIss(resultSet.getBigDecimal("ISS_CALCULADO"));
            if (retido) {
                dto.setTotalIssRetido(resultSet.getBigDecimal("ISS_CALCULADO"));
            }
            dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
            dto.setNatureza(resultSet.getString("NATUREZAOPERACAO"));
            dto.setQtdNotas(resultSet.getInt("QUANTIDADE"));
            return dto;
        });
        dms.addNaturezas(naturezas);
        return dms;
    }

    public ResumoDMSDTO buscarResumoPorDMS(Long dmsId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("DMS_ID", dmsId);

        String sql = "  SELECT  " +
                "    COUNT(DEC.ID) AS QUANTIDADE," +
                "    SUM(COALESCE(DEC.TOTALSERVICOS, 0)) AS TOTAL_SERVICOS," +
                "    SUM(COALESCE(COALESCE(DEC.DESCONTOSINCONDICIONAIS, DEC.DESCONTOSCONDICIONAIS), 0)) AS DESCONTO," +
                "    SUM(COALESCE(DEC.DEDUCOESLEGAIS, 0)) AS DEDUCOES," +
                "    SUM(COALESCE(DEC.VALORLIQUIDO, 0)) AS VALORLIQUIDO," +
                "    SUM(COALESCE(DEC.BASECALCULO, 0)) AS BASE_CALCULO," +
                "    SUM(COALESCE(DEC.ISSRETIDO, 0)) AS ISS_RETIDO," +
                "    SUM(COALESCE(DEC.ISSCALCULADO, 0)) AS ISS_CALCULADO," +
                "    SUM(COALESCE(DEC.ISSPAGAR, 0)) AS ISS_PAGAR," +
                "    DEC.NATUREZAOPERACAO " +
                "  FROM DECLARACAOPRESTACAOSERVICO DEC  " +
                " INNER JOIN NOTADECLARADA ND ON ND.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                " WHERE ND.DECLARACAOMENSALSERVICO_ID = :DMS_ID " +
                " GROUP BY DEC.NATUREZAOPERACAO ";

        ResumoDMSDTO dms = new ResumoDMSDTO();

        List<ResumoNaturezaDTO> naturezas = namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> {
            ResumoNaturezaDTO dto = new ResumoNaturezaDTO();
            boolean retido = resultSet.getBoolean("ISS_RETIDO");
            dto.setTotalIssDevido(resultSet.getBigDecimal("ISS_PAGAR"));
            dto.setTotalIss(resultSet.getBigDecimal("ISS_CALCULADO"));
            if (retido) {
                dto.setTotalIssRetido(resultSet.getBigDecimal("ISS_CALCULADO"));
            }
            dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
            dto.setNatureza(resultSet.getString("NATUREZAOPERACAO"));
            dto.setQtdNotas(resultSet.getInt("QUANTIDADE"));
            return dto;
        });
        dms.addNaturezas(naturezas);
        return dms;
    }

    public List<NotaFiscalSearchDTO> buscarNotasPorCompetenciaServicosTomados(Long prestadorId, Integer exercicio,
                                                                              Integer mes, boolean declarada,
                                                                              int page, int size) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("PRESTADOR_ID", prestadorId);
        parameters.addValue("EXERCICIO", exercicio);
        parameters.addValue("MES", mes);
        parameters.addValue("TIPO_SERVICO_TOMADO", TipoServicoDeclaradoNfseDTO.SERVICO_TOMADO.name());
        parameters.addValue("CANCELADA", SituacaoDeclaracaoNfseDTO.CANCELADA.name());

        String sql = "  SELECT  " +
                "   DEC.ID, " +
                "   DEC.TIPODOCUMENTO AS TIPO,  " +
                "   COALESCE(NOTA.NUMERO, SDP.NUMERO) AS NUMERO,  " +
                "   COALESCE(NOTA.EMISSAO, SDP.EMISSAO) AS EMISSAO,  " +
                "   DPP.NOMERAZAOSOCIAL AS PRESTADOR_NOMERAZAOSOCIAL,  " +
                "   DPP.CPFCNPJ AS PRESTADOR_CPFCNPJ,  " +
                "   DPT.NOMERAZAOSOCIAL AS TOMADOR_NOMERAZAOSOCIAL,  " +
                "   DPT.CPFCNPJ AS TOMADOR_CPFCNPJ,  " +
                "   DEC.SITUACAO, " +
                "   COALESCE(DEC.TOTALSERVICOS, 0) AS TOTAL_SERVICOS,  " +
                "   COALESCE(COALESCE(DEC.DESCONTOSINCONDICIONAIS, DEC.DESCONTOSCONDICIONAIS), 0) AS DESCONTO, " +
                "   COALESCE(DEC.DEDUCOESLEGAIS, 0) AS DEDUCOES, " +
                "   COALESCE(DEC.VALORLIQUIDO, 0) AS VALORLIQUIDO,  " +
                "   COALESCE(DEC.BASECALCULO, 0) AS BASE_CALCULO,  " +
                "   COALESCE(IDS.ALIQUOTASERVICO, 0) AS ALIQUOTA,  " +
                "   COALESCE(DEC.ISSRETIDO, 0) AS ISS_RETIDO,  " +
                "   COALESCE(DEC.ISSCALCULADO, 0) AS ISS_CALCULADO,  " +
                "   COALESCE(DEC.ISSPAGAR, 0) AS ISS_PAGAR,  " +
                "   COALESCE(NOTA.PRESTADOR_ID, SDP.CADASTROECONOMICO_ID) AS ID_PRESTADOR, " +
                "   DEC.NATUREZAOPERACAO  " +
                "  FROM DECLARACAOPRESTACAOSERVICO DEC  " +
                " INNER JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
                " LEFT JOIN NOTAFISCAL NOTA ON NOTA.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID  " +
                " LEFT JOIN SERVICODECLARADO SDP ON SDP.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID AND SDP.TIPOSERVICODECLARADO = :TIPO_SERVICO_TOMADO " +
                " LEFT JOIN DADOSPESSOAISNFSE DPP ON DPP.ID = DEC.DADOSPESSOAISPRESTADOR_ID  " +
                " LEFT JOIN DADOSPESSOAISNFSE DPT ON DPT.ID = DEC.DADOSPESSOAISTOMADOR_ID " +
                " LEFT JOIN PESSOAJURIDICA PJT ON PJT.CNPJ = DPT.CPFCNPJ " +
                " INNER JOIN CADASTROECONOMICO CE ON CE.PESSOA_ID = PJT.ID " +
                "WHERE CE.ID =:PRESTADOR_ID  " +
                "   AND COALESCE(NOTA.HOMOLOGACAO, 0) = 0 " +
                "   AND DEC.SITUACAO != :CANCELADA " +
                "   AND DEC.ISSRETIDO = 1 " +
                "   AND EXTRACT(YEAR FROM DEC.COMPETENCIA) = :EXERCICIO " +
                "   AND EXTRACT(MONTH FROM DEC.COMPETENCIA) = :MES ";
        if (declarada) {
            sql += " AND (FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.RETENCAO.name() + "', DEC.ID) = 1) ";
        } else {
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.RETENCAO.name() + "', DEC.ID) = 0 ";
            sql += " AND FUNC_NOTA_DECLARADA('" + TipoMovimentoMensalNfseDTO.ISS_RETIDO.name() + "', DEC.ID) = 0 ";
        }
        sql += " ORDER BY DEC.TIPODOCUMENTO, COALESCE(NOTA.NUMERO, SDP.NUMERO) OFFSET " + (page * size) + " ROWS FETCH NEXT " + size + " ROWS ONLY ";


        return namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> popularNotaFiscalSearchDTO(resultSet));
    }

    public List<NotaFiscalSearchDTO> buscarNotasByDeclaracaoMensalServico(Long idDms) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_dms", idDms);

        String sql = "select " +
                "    coalesce(nf.id, sd.id) as id_nota, " +
                "    dps.tipodocumento as tipo_documento, " +
                "    cep.id as id_prestador, " +
                "    tnf.id as id_tomador," +
                "    rps.id as id_rps," +
                "    rps.numero as numero_rps," +
                "    coalesce(nf.numero, sd.numero) as numero_nota," +
                "    coalesce(nf.emissao, sd.emissao) as emissao_nota," +
                "    dptnf.nomerazaosocial as nome_tomador," +
                "    dppnf.nomerazaosocial as nome_prestador," +
                "    dptnf.cpfcnpj as cpfcnpj_tomador," +
                "    dppnf.cpfcnpj as cpfcnpj_prestador," +
                "    dps.situacao as situacao," +
                "    dps.modalidade as modalidade," +
                "    dps.issretido as issretido," +
                "    dps.totalservicos as totalservicos," +
                "    dps.deducoeslegais as deducoeslegais," +
                "    dps.basecalculo as basecalculo," +
                "    dps.isspagar as isspagar," +
                "    dps.isscalculado as isscalculado, " +
                "    dps.naturezaoperacao as naturezaoperacao, " +
                "    nf.descriminacaoServico as descriminacaoservico, " +
                "    rps.serie as serie_rps, " +
                "    rps.tipoRps as tipo_rps " +
                "   from declaracaoprestacaoservico dps " +
                "  left join notafiscal nf on nf.declaracaoprestacaoservico_id = dps.id " +
                "  left join servicodeclarado sd on sd.declaracaoprestacaoservico_id = dps.id " +
                "  left join intermediarioservico i on i.id = dps.intermediario_id " +
                "  left join rps rps on rps.id = nf.rps_id " +
                "  left join tomadorserviconfse tnf on tnf.id = nf.tomador_id " +
                "  left join dadospessoaisnfse dptnf on dptnf.id = dps.dadospessoaistomador_id " +
                "  left join dadospessoaisnfse dppnf on dppnf.id = dps.dadospessoaisprestador_id " +
                "  inner join cadastroeconomico cep on cep.id = coalesce(nf.prestador_id, sd.cadastroeconomico_id) " +
                "  inner join notadeclarada nd on nd.declaracaoprestacaoservico_id = dps.id " +
                " where nd.declaracaomensalservico_id = :id_dms ";

        return namedParameterJdbcTemplate.query(sql, parameters, new NotaFiscalSearchDTO());
    }
}
