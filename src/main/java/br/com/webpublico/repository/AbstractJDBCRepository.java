package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public abstract class AbstractJDBCRepository<T extends AbstractEntity> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    IdJDBCRepository idJDBCRepository;

    public abstract String getSelect();

    public abstract String getFrom();

    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    public abstract RowMapper<T> newRowMapper();

    public T findById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append(" where obj.id = :id ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        List<T> query = namedParameterJdbcTemplate.query(sql.toString(), parameters, newRowMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<T> consultar(Pageable pageable, List<ParametroQuery> parametros, String orderBy) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        } catch (Exception e) {
            log.error("Erro ao montar parametros para pesquisa: {}", e);
        }
        if (Strings.isNullOrEmpty(orderBy)) {
            orderBy = getOrderByDefault();
        }
        sql.append(orderBy);
        if (pageable != null) {
            parameters.addValue("offset", pageable.getOffset());
            parameters.addValue("limit", pageable.getPageSize());
            sql.append(" offset :offset rows fetch first :limit rows only ");
        }
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, newRowMapper());
    }

    public Integer contar(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(obj.id) as value ").append(getFrom());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        return namedParameterJdbcTemplate.queryForObject(sql.toString(),
                parameters, Integer.class);
    }

    public Page<T> consultarPaginado(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        List<T> registros = consultar(pageable, parametros, orderBy);
        Integer count = contar(parametros);
        return new PageImpl<>(registros, pageable, count);
    }

    public T findByAtributo(String atributo, Object valor) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append(" where ").append(atributo).append(" = :valor ")
                .append(" fetch first 1 rows only ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("valor", valor);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql.toString(), parameters, newRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public abstract T insert(T dto);

    public abstract T update(T dto);

    public abstract void remove(T dto);

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public List<T> buscarFiltrando(String parte, String... filtros) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom());
        String juncao = " where ";
        for (String filtro : filtros) {
            sql.append(juncao).append("lower(").append(filtro).append(") like lower(:parte) ");
            juncao = " or ";
        }
        sql.append(getOrderByDefault());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("parte", "%" + parte.trim() + "%");
        return namedParameterJdbcTemplate.query(sql.toString(), parameterSource, newRowMapper());
    }
}
