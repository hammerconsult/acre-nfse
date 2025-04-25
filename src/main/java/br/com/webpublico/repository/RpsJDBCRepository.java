package br.com.webpublico.repository;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.RpsNfseDTO;
import br.com.webpublico.domain.dto.XmlWsNfseRecebido;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.LongMapper;
import br.com.webpublico.repository.mapper.RpsMapper;
import br.com.webpublico.repository.mongo.XmlWsNfseMongoRepository;
import br.com.webpublico.repository.setter.DetalheItemRpsSetter;
import br.com.webpublico.repository.setter.ItemRpsSetter;
import br.com.webpublico.repository.setter.RpsSetter;
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
public class RpsJDBCRepository implements Serializable {

    private static final String SQL_FIELDS = " SELECT " +
            " R.ID AS ID," +
            " R.NUMERO AS NUMERO, " +
            " R.SERIE AS SERIE, " +
            " R.DATAEMISSAO AS DATAEMISSAO, " +
            " R.PRESTADOR_ID AS PRESTADOR_ID, " +
            " R.NATUREZAOPERACAO AS NATUREZAOPERACAO, " +
            " R.OPTANTESIMPLESNACIONAL AS OPTANTESIMPLESNACIONAL, " +
            " R.COMPETENCIA AS COMPETENCIA," +
            " R.DADOSPESSOAISPRESTADOR_ID, " +
            " R.DADOSPESSOAISTOMADOR_ID, " +
            " R.TRIBUTOSFEDERAIS_ID AS TRIBUTOSFEDERAIS_ID, " +
            " R.ISSRETIDO AS ISSRETIDO," +
            " R.TOTALSERVICOS AS TOTALSERVICOS, " +
            " R.DEDUCOESLEGAIS AS DEDUCOESLEGAIS, " +
            " R.DESCONTOSINCONDICIONAIS AS DESCONTOSINCONDICIONAIS, " +
            " R.DESCONTOSCONDICIONAIS AS DESCONTOSCONDICIONAIS, " +
            " R.RETENCOESFEDERAIS AS RETENCOESFEDERAIS, " +
            " R.BASECALCULO AS BASECALCULO, " +
            " R.ISSCALCULADO AS ISSCALCULADO, " +
            " R.ISSPAGAR AS ISSPAGAR, " +
            " R.VALORLIQUIDO AS VALORLIQUIDO, " +
            " R.LOTERPS_ID AS LOTERPS_ID, " +
            " R.DESCRIMINACAOSERVICO AS DESCRIMINACAOSERVICO, " +
            " R.TIPORPS AS TIPORPS, " +
            " R.INCENTIVOFISCAL AS INCENTIVOFISCAL, " +
            " R.CONSTRUCAOCIVIL_ID AS CONSTRUCAOCIVIL_ID," +
            " N.NUMERO AS NUMERO_NOTA_FISCAL, " +
            " DEC.SITUACAO AS SITUACAO_NOTA_FISCAL, " +
            " N.id AS ID_NOTA_FISCAL, " +
            " LOTE.NUMERO AS NUMERO_LOTE ";
    private static final String SQL_FROM = " FROM RPS R " +
            " INNER JOIN CADASTROECONOMICO CE ON CE.ID = R.PRESTADOR_ID " +
            " LEFT JOIN NOTAFISCAL N ON N.RPS_ID = R.ID " +
            " LEFT JOIN LOTERPS LOTE ON LOTE.ID = R.LOTERPS_ID " +
            " LEFT JOIN DECLARACAOPRESTACAOSERVICO DEC ON DEC.ID = N.DECLARACAOPRESTACAOSERVICO_ID" +
            " LEFT JOIN ITEMDECLARACAOSERVICO IDS ON IDS.DECLARACAOPRESTACAOSERVICO_ID = DEC.ID " +
            " LEFT JOIN DADOSPESSOAISNFSE DPTNF ON DPTNF.ID = DEC.DADOSPESSOAISTOMADOR_ID " +
            " LEFT JOIN DADOSPESSOAISNFSE DPPNF ON DPPNF.ID = DEC.DADOSPESSOAISPRESTADOR_ID ";


    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;
    @Autowired
    private XmlWsNfseMongoRepository xmlWsNfseMongoRepository;

    public RpsNfseDTO findById(Long id) {
        List<RpsNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                        " WHERE R.ID = ? ",
                new Object[]{id},
                new RpsMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public RpsNfseDTO findByCnpjNumeroSerie(String cnpj, String numero, String serie) {
        String sql = SQL_FIELDS + SQL_FROM +
                " WHERE DPPNF.CPFCNPJ = ? AND R.NUMERO = ? ";
        Object[] params = null;
        if (!Strings.isNullOrEmpty(serie)) {
            sql += " AND R.SERIE = ? ";
            params = new Object[]{StringUtils.removerMascaraCpfCnpj(cnpj), numero, serie};
        } else {
            sql += " AND R.SERIE IS NULL ";
            params = new Object[]{StringUtils.removerMascaraCpfCnpj(cnpj), numero};
        }
        List<RpsNfseDTO> query = jdbcTemplate.query(sql,
                params,
                new RpsMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public RpsNfseDTO findByCmcNumeroSerie(String cmc, String numero, String serie) {
        String sql = SQL_FIELDS + SQL_FROM +
                " WHERE CE.INSCRICAOCADASTRAL = ? AND R.NUMERO = ? ";
        Object[] params = null;
        if (!Strings.isNullOrEmpty(serie)) {
            sql += " AND R.SERIE = ? ";
            params = new Object[]{cmc, numero, serie};
        } else {
            sql += " AND R.SERIE IS NULL ";
            params = new Object[]{cmc, numero};
        }
        List<RpsNfseDTO> query = jdbcTemplate.query(sql,
                params,
                new RpsMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public Long getUltimoNumeroRps(Long prestadorId) {
        List<Long> query = jdbcTemplate.query(" SELECT MAX(to_number(R.NUMERO)) AS VALUE FROM RPS R " +
                        " WHERE R.PRESTADOR_ID = ? ",
                new Object[]{prestadorId},
                new LongMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0L;
    }

    public void inserir(RpsNfseDTO rps) {
        rps.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO RPS " +
                        " (ID, NUMERO, SERIE, DATAEMISSAO, PRESTADOR_ID, NATUREZAOPERACAO, OPTANTESIMPLESNACIONAL, " +
                        " COMPETENCIA, DADOSPESSOAISPRESTADOR_ID, DADOSPESSOAISTOMADOR_ID, TRIBUTOSFEDERAIS_ID, ISSRETIDO, " +
                        " TOTALSERVICOS, DEDUCOESLEGAIS, DESCONTOSINCONDICIONAIS, DESCONTOSCONDICIONAIS, " +
                        " RETENCOESFEDERAIS, BASECALCULO, ISSCALCULADO, ISSPAGAR, VALORLIQUIDO, LOTERPS_ID, " +
                        " DESCRIMINACAOSERVICO, TIPORPS, INCENTIVOFISCAL, CONSTRUCAOCIVIL_ID, TOTALNOTA) " +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new RpsSetter(rps));
    }

    public List<RpsNfseDTO> consultarRps(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        StringBuilder sql = new StringBuilder().append(SQL_FIELDS).append(SQL_FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        if (Strings.isNullOrEmpty(orderBy)) {
            orderBy = "ORDER BY to_number(r.numero) DESC";
        }
        sql.append(orderBy);
        if (pageable != null) {
            parameters.addValue("offset", pageable.getOffset());
            parameters.addValue("limit", pageable.getPageSize());
            sql.append(" offset :offset rows fetch first :limit rows only ");
        }
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new RpsMapper());
    }

    public Integer contarRps(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(r.id) as value ").append(SQL_FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

}
