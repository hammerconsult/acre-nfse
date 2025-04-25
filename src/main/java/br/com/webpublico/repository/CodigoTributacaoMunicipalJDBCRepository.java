package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoMunicipalNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoNfseDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public class CodigoTributacaoMunicipalJDBCRepository extends AbstractJDBCRepository<CodigoTributacaoMunicipalNfseDTO> {

    @Override
    public String getSelect() {
        return " select obj.id, " +
                " obj.codigo, " +
                " obj.codigotributacao_id, " +
                " obj.aliquota, " +
                " obj.iniciovigencia, " +
                " obj.fimvigencia  ";
    }

    @Override
    public String getFrom() {
        return " from codigotributacaomunicipal obj ";
    }

    @Override
    public RowMapper<CodigoTributacaoMunicipalNfseDTO> newRowMapper() {
        return new CodigoTributacaoMunicipalNfseDTO();
    }

    @Override
    public CodigoTributacaoMunicipalNfseDTO insert(CodigoTributacaoMunicipalNfseDTO dto) {
        return dto;
    }

    @Override
    public CodigoTributacaoMunicipalNfseDTO update(CodigoTributacaoMunicipalNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(CodigoTributacaoMunicipalNfseDTO dto) {
    }

    public CodigoTributacaoMunicipalNfseDTO findByCodigoTributacaoAndAliquota(CodigoTributacaoNfseDTO codigoTributacao,
                                                                              BigDecimal aliquota) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append(" where obj.codigotributacao_id = :codigotributacao_id ")
                .append("   and obj.aliquota = :aliquota ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("codigotributacao_id", codigoTributacao.getId());
        parameters.addValue("aliquota", aliquota);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql.toString(), parameters, newRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public CodigoTributacaoMunicipalNfseDTO findByCodigoTributacaoAndVigencia(CodigoTributacaoNfseDTO codigoTributacao,
                                                                              Date inicioVigencia,
                                                                              Date fimVigencia) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append(" where obj.codigotributacao_id = :codigotributacao_id ")
                .append("   and ( ")
                .append("         (obj.iniciovigencia <= :inicio_vigencia and coalesce(obj.fimvigencia, current_date) >= :fim_vigencia) or  ")
                .append("         (obj.iniciovigencia <= :inicio_vigencia and coalesce(obj.fimvigencia, current_date) < :fim_vigencia) or ")
                .append("         (obj.iniciovigencia > :inicio_vigencia and coalesce(obj.fimvigencia, current_date) >= :fim_vigencia) or ")
                .append("         (obj.iniciovigencia >= :inicio_vigencia and coalesce(obj.fimvigencia, current_date) <= :fim_vigencia) ")
                .append("       ) ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("codigotributacao_id", codigoTributacao.getId());
        parameters.addValue("inicio_vigencia", inicioVigencia);
        parameters.addValue("fim_vigencia", fimVigencia);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql.toString(), parameters, newRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
