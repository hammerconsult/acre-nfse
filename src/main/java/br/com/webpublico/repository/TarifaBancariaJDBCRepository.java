package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.TarifaBancariaNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TarifaBancariaJDBCRepository extends AbstractJDBCRepository<TarifaBancariaNfseDTO> {

    @Override
    public String getSelect() {
        return " select obj.id," +
                " obj.codigo," +
                " obj.descricao," +
                " obj.grupo, " +
                " obj.periodicidade ";
    }

    @Override
    public String getFrom() {
        return " from tarifabancaria obj ";
    }

    @Override
    public RowMapper<TarifaBancariaNfseDTO> newRowMapper() {
        return new TarifaBancariaNfseDTO();
    }

    @Override
    public TarifaBancariaNfseDTO insert(TarifaBancariaNfseDTO dto) {
        return dto;
    }

    @Override
    public TarifaBancariaNfseDTO update(TarifaBancariaNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(TarifaBancariaNfseDTO dto) {

    }
}
