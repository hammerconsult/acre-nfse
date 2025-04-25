package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConfiguracaoWebServiceNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ConfiguracaoWebServiceJDBCRepository extends AbstractJDBCRepository<ConfiguracaoWebServiceNfseDTO> {

    @Override
    public String getSelect() {
        return " select obj.id," +
                " obj.chave," +
                " obj.url," +
                " obj.usuario," +
                " obj.senha," +
                " obj.detalhe ";
    }

    @Override
    public String getFrom() {
        return " from configuracaowebservice obj ";
    }

    @Override
    public RowMapper<ConfiguracaoWebServiceNfseDTO> newRowMapper() {
        return new ConfiguracaoWebServiceNfseDTO();
    }

    @Override
    public ConfiguracaoWebServiceNfseDTO insert(ConfiguracaoWebServiceNfseDTO dto) {
        return dto;
    }

    @Override
    public ConfiguracaoWebServiceNfseDTO update(ConfiguracaoWebServiceNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ConfiguracaoWebServiceNfseDTO dto) {

    }
}
