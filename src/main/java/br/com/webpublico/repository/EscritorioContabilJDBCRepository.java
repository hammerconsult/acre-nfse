package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.EscritorioContabilNfseDTO;
import br.com.webpublico.repository.mapper.EscritorioContabilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class EscritorioContabilJDBCRepository extends AbstractJDBCRepository<EscritorioContabilNfseDTO> {

    @Override
    public String getSelect() {
        return " SELECT OBJ.ID AS ID," +
                " OBJ.CODIGO AS CODIGO," +
                " OBJ.CRC AS CRCCONTADOR," +
                " OBJ.CRCESCRITORIO AS CRCESCRITORIO," +
                " OBJ.PESSOA_ID AS PESSOA_ID," +
                " OBJ.RESPONSAVEL_ID AS RESPONSAVEL_ID ";
    }

    @Override
    public String getFrom() {
        return " FROM ESCRITORIOCONTABIL OBJ ";
    }

    @Override
    public RowMapper<EscritorioContabilNfseDTO> newRowMapper() {
        return new EscritorioContabilMapper();
    }

    @Override
    public EscritorioContabilNfseDTO insert(EscritorioContabilNfseDTO dto) {
        return null;
    }

    @Override
    public EscritorioContabilNfseDTO update(EscritorioContabilNfseDTO dto) {
        return null;
    }

    @Override
    public void remove(EscritorioContabilNfseDTO dto) {

    }

}
