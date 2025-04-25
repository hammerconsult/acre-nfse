package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ProdutoServicoBancarioNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutoServicoBancarioJDBCRepository extends AbstractJDBCRepository<ProdutoServicoBancarioNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id," +
                " obj.codigo," +
                " obj.descricao," +
                " obj.grupo, " +
                " obj.obrigadescricaocomplementar ";
    }

    @Override
    public String getFrom() {
        return " from produtoservicobancario obj ";
    }

    @Override
    public RowMapper<ProdutoServicoBancarioNfseDTO> newRowMapper() {
        return new ProdutoServicoBancarioNfseDTO();
    }

    @Override
    public ProdutoServicoBancarioNfseDTO insert(ProdutoServicoBancarioNfseDTO dto) {
        return dto;
    }

    @Override
    public ProdutoServicoBancarioNfseDTO update(ProdutoServicoBancarioNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ProdutoServicoBancarioNfseDTO dto) {
        
    }
}
