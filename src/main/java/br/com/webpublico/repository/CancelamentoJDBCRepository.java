package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.CancelamentoNfseDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.domain.dto.enums.SituacaoDeferimentoNfseDTO;
import br.com.webpublico.repository.mapper.CancelamentoMapper;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.setter.CancelamentoSetter;
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
public class CancelamentoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IdJDBCRepository idJDBCRepository;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final String from = " FROM CANCDECLAPRESTACAOSERVICO canc " +
            " inner join declaracaoprestacaoservico dps on dps.id = canc.declaracao_id" +
            " inner join notafiscal nf on nf.declaracaoprestacaoservico_id = dps.id ";

    public CancelamentoNfseDTO findById(Long id) {
        List<CancelamentoNfseDTO> query = jdbcTemplate.query(" SELECT ID, DATACANCELAMENTO, MOTIVOCANCELAMENTO, " +
                        " OBSERVACOESCANCELAMENTO, SITUACAOFISCAL, SITUACAOTOMADOR, SOLICITANTE, OBSERVACOESFISCAL " +
                        " FROM CANCDECLAPRESTACAOSERVICO where id = ?",
                new Object[]{id},
                new CancelamentoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<CancelamentoNfseDTO> findByNotaId(Long id) {
        List<CancelamentoNfseDTO> query = jdbcTemplate.query(" SELECT " +
                        " canc.ID, " +
                        " canc.DATACANCELAMENTO, " +
                        " canc.MOTIVOCANCELAMENTO, " +
                        " canc.OBSERVACOESCANCELAMENTO, " +
                        " canc.SITUACAOFISCAL, " +
                        " canc.SITUACAOTOMADOR, " +
                        " canc.SOLICITANTE, " +
                        " coalesce(canc.OBSERVACOESFISCAL,lote.observacoesFiscal) as OBSERVACOESFISCAL" +
                        " FROM CANCDECLAPRESTACAOSERVICO canc " +
                        " inner join declaracaoprestacaoservico dps on dps.id = canc.declaracao_id" +
                        " left join LoteCancelamentoDeclaracaoPrestacaoServico lote on lote.id = canc.lote_id" +
                        " inner join notafiscal nf on nf.declaracaoprestacaoservico_id = dps.id " +
                        " where nf.id = ?",
                new Object[]{id},
                new CancelamentoMapper());
        return query;
    }

    public List<CancelamentoNfseDTO> findByDeclaracaoId(Long id) {
        List<CancelamentoNfseDTO> query = jdbcTemplate.query(" SELECT " +
                        " canc.ID, " +
                        " canc.DATACANCELAMENTO, " +
                        " canc.MOTIVOCANCELAMENTO, " +
                        " canc.OBSERVACOESCANCELAMENTO, " +
                        " canc.SITUACAOFISCAL, " +
                        " canc.SITUACAOTOMADOR, " +
                        " canc.SOLICITANTE, " +
                        " coalesce(canc.OBSERVACOESFISCAL,lote.observacoesFiscal) as OBSERVACOESFISCAL" +
                        " FROM CANCDECLAPRESTACAOSERVICO canc " +
                        " inner join declaracaoprestacaoservico dps on dps.id = canc.declaracao_id" +
                        " left join LoteCancelamentoDeclaracaoPrestacaoServico lote on lote.id = canc.lote_id" +
                        " where dps.id = ?",
                new Object[]{id},
                new CancelamentoMapper());
        return query;
    }

    public List<CancelamentoNfseDTO> findByDeclaracaoIdAndSituacao(Long id, SituacaoDeferimentoNfseDTO situacao) {
        List<CancelamentoNfseDTO> query = jdbcTemplate.query(" SELECT " +
                        " canc.ID, " +
                        " canc.DATACANCELAMENTO, " +
                        " canc.MOTIVOCANCELAMENTO, " +
                        " canc.OBSERVACOESCANCELAMENTO, " +
                        " canc.SITUACAOFISCAL, " +
                        " canc.SITUACAOTOMADOR, " +
                        " canc.SOLICITANTE, " +
                        " coalesce(canc.OBSERVACOESFISCAL,lote.observacoesFiscal) as OBSERVACOESFISCAL" +
                        " FROM CANCDECLAPRESTACAOSERVICO canc " +
                        " inner join declaracaoprestacaoservico dps on dps.id = canc.declaracao_id" +
                        " left join LoteCancelamentoDeclaracaoPrestacaoServico lote on lote.id = canc.lote_id" +
                        " where dps.id = ? and canc.situacaoFiscal = ?",
                new Object[]{id, situacao.name()},
                new CancelamentoMapper());
        return query;
    }

    public CancelamentoNfseDTO inserir(CancelamentoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO CANCDECLAPRESTACAOSERVICO " +
                " (ID, DATACANCELAMENTO, MOTIVOCANCELAMENTO, SITUACAOFISCAL, OBSERVACOESCANCELAMENTO, " +
                " OBSERVACOESFISCAL, DATADEFERIMENTOFISCAL, DECLARACAOSUBSTITUTA_ID, USUARIOTOMADOR_ID, " +
                " SITUACAOTOMADOR, DATADEFERIMENTOTOMADOR, TIPOCANCELAMENTO, SOLICITANTE, DECLARACAO_ID, " +
                " TIPODOCUMENTO) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)  ", new CancelamentoSetter(dto));
        return dto;
    }

    public List<CancelamentoNfseDTO> findNaoVisualizadosByPrestadorId(Long id) {
        List<CancelamentoNfseDTO> query = jdbcTemplate.query(" SELECT " +
                        " canc.ID, " +
                        " canc.DATACANCELAMENTO, " +
                        " canc.MOTIVOCANCELAMENTO, " +
                        " canc.OBSERVACOESCANCELAMENTO, " +
                        " canc.SITUACAOFISCAL, " +
                        " canc.SITUACAOTOMADOR, " +
                        " canc.SOLICITANTE, " +
                        " coalesce(canc.OBSERVACOESFISCAL,lote.observacoesFiscal) as OBSERVACOESFISCAL" +
                        " nf.id as nota_id," +
                        " nf.numero as nota_numero," +
                        " nf.emissao as nota_emissao " +
                        " FROM CANCDECLAPRESTACAOSERVICO canc " +
                        " inner join declaracaoprestacaoservico dps on dps.id = canc.declaracao_id" +
                        " inner join notafiscal nf on nf.declaracaoprestacaoservico_id = dps.id " +
                        " left join LoteCancelamentoDeclaracaoPrestacaoServico lote on lote.id = canc.lote_id" +
                        " where canc.visualizadoPeloContribuinte = 0 and  nf.prestador_id = ?",
                new Object[]{id},
                new CancelamentoMapper());
        return query;
    }

    public Integer contarCacnelamentos(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(canc.id) as value ").append(from);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public List<CancelamentoNfseDTO> findByParam(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {


        String select = " SELECT " +
                " canc.ID, " +
                " canc.DATACANCELAMENTO, " +
                " canc.MOTIVOCANCELAMENTO, " +
                " canc.OBSERVACOESCANCELAMENTO, " +
                " canc.SITUACAOFISCAL, " +
                " canc.SITUACAOTOMADOR, " +
                " canc.SOLICITANTE, " +
                " canc.OBSERVACOESFISCAL," +
                " nf.id as nota_id," +
                " nf.numero as nota_numero," +
                " nf.emissao as nota_emissao ";


        StringBuilder sql = new StringBuilder(select);
        sql.append(from);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        if (Strings.isNullOrEmpty(orderBy)) {
            orderBy = " ORDER BY NF.NUMERO DESC ";
        }
        sql.append(orderBy);
        parameters.addValue("OFFSET", pageable.getOffset());
        parameters.addValue("LIMIT", pageable.getPageSize());
        sql.append(" OFFSET :OFFSET ROWS FETCH FIRST :LIMIT ROWS ONLY ");
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new CancelamentoMapper());
    }

    public void marcarCancelamentoVisualizado(CancelamentoNfseDTO dto) {
        jdbcTemplate.update(" UPDATE CANCDECLAPRESTACAOSERVICO " +
                " SET visualizadoPeloContribuinte = 1  WHERE ID = ? ", new Object[]{dto.getId()});
    }
}
