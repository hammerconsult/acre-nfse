package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CodigoTributacaoJDBCRepository extends AbstractJDBCRepository<CodigoTributacaoNfseDTO> {

    @Override
    public String getSelect() {
        return " select obj.id," +
                " obj.codigo," +
                " obj.descricao," +
                " obj.servico_id ";
    }

    @Override
    public String getFrom() {
        return " from codigotributacao obj ";
    }

    @Override
    public RowMapper<CodigoTributacaoNfseDTO> newRowMapper() {
        return new CodigoTributacaoNfseDTO();
    }

    @Override
    public CodigoTributacaoNfseDTO insert(CodigoTributacaoNfseDTO dto) {
        return dto;
    }

    @Override
    public CodigoTributacaoNfseDTO update(CodigoTributacaoNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(CodigoTributacaoNfseDTO dto) {
        
    }
}
