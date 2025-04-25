package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ManualDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ManualJDBCRepository extends AbstractJDBCRepository<ManualDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id," +
                " tm.id as tipomanual_id, " +
                " tm.descricao as tipomanual_descricao, " +
                " tm.ordem as tipomanual_ordem, " +
                " obj.nome, " +
                " obj.resumo, " +
                " obj.link, " +
                " obj.ordem, " +
                " obj.habilitarexibicao, " +
                " a.id as arquivo_id, " +
                " a.nome as arquivo_nome ";
    }

    @Override
    public String getFrom() {
        return "    from manualnfse obj" +
                " inner join tipomanual tm on tm.id = obj.tipomanual_id" +
                " left join detentorarquivocomposicao dac on dac.id = obj.detentorarquivocomposicao_id" +
                " left join arquivocomposicao ac on ac.detentorarquivocomposicao_id = dac.id " +
                " left join arquivo a on a.id = ac.arquivo_id ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by tm.ordem, obj.ordem ";
    }

    @Override
    public RowMapper<ManualDTO> newRowMapper() {
        return new ManualDTO();
    }

    @Override
    public ManualDTO insert(ManualDTO dto) {
        return null;
    }

    @Override
    public ManualDTO update(ManualDTO dto) {
        return null;
    }

    @Override
    public void remove(ManualDTO dto) {

    }

    public List<ManualDTO> buscarManuaisParaExibicao() {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append("  where obj.habilitarexibicao = 1 ")
                .append("    and tm.habilitarexibicao = 1 ")
                .append(" order by obj.ordem ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, newRowMapper());
    }

    public List<ManualDTO> buscarManuaisPorTag(String tag) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append("  where obj.habilitarexibicao = 1 ")
                .append("    and tm.habilitarexibicao = 1 ")
                .append("    and lower(m.tags) like :tags ")
                .append(" order by obj.ordem ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("tags", "%" + tag.trim().toLowerCase() + "%");

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, newRowMapper());
    }

    public List<ManualDTO> buscarManuaisPorTipo(Long tipo) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append("  where obj.habilitarexibicao = 1 ")
                .append("    and tm.id = :tipo ")
                .append(" order by obj.ordem ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("tipo", tipo);

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, newRowMapper());
    }
}
