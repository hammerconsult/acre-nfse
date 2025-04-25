package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.LegislacaoDTO;
import br.com.webpublico.domain.dto.ManualDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LegislacaoJDBCRepository extends AbstractJDBCRepository<LegislacaoDTO> {
    @Override
    public String getSelect() {
        return " select " +
                "  obj.id, " +
                "  tl.id as tipo_id, " +
                "  tl.descricao as tipo_descricao," +
                "  tl.habilitarexibicao as tipo_habilitarexibicao," +
                "  tl.ordemexibicao as tipo_ordemexibicao, " +
                "  obj.datapublicacao, " +
                "  obj.nome, " +
                "  obj.sumula, " +
                "  obj.habilitarexibicao, " +
                "  obj.ordemexibicao, " +
                "  obj.ordemexibicao, " +
                "  a.id as arquivo_id, " +
                "  a.nome as arquivo_nome ";
    }

    @Override
    public String getFrom() {
        return " from legislacao obj " +
                " left join tipolegislacao tl on tl.id = obj.tipolegislacao_id " +
                " left join detentorarquivocomposicao dac on dac.id = obj.detentorarquivocomposicao_id" +
                " left join arquivocomposicao ac on ac.detentorarquivocomposicao_id = dac.id " +
                " left join arquivo a on a.id = ac.arquivo_id ";
    }

    @Override
    public RowMapper<LegislacaoDTO> newRowMapper() {
        return new LegislacaoDTO();
    }

    @Override
    public LegislacaoDTO insert(LegislacaoDTO dto) {
        return null;
    }

    @Override
    public LegislacaoDTO update(LegislacaoDTO dto) {
        return null;
    }

    @Override
    public void remove(LegislacaoDTO dto) {
    }

    public List<LegislacaoDTO> buscarLegilacoesParaExibicao() {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append("  where obj.habilitarexibicao = 1 ")
                .append("    and tl.habilitarexibicao = 1 ")
                .append(" order by tl.ordemexibicao, obj.ordemexibicao ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, newRowMapper());
    }
}
