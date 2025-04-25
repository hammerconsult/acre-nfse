package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConfiguracaoDeRelatorioNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ConfiguracaoDeRelatorioJDBCRepository extends AbstractJDBCRepository<ConfiguracaoDeRelatorioNfseDTO> {

    @Override
    public String getSelect() {
        return " select obj.id, obj.url, obj.urlwebpublico, obj.chave ";
    }

    @Override
    public String getFrom() {
        return " from configuracaoderelatorio obj ";
    }

    @Override
    public RowMapper<ConfiguracaoDeRelatorioNfseDTO> newRowMapper() {
        return new ConfiguracaoDeRelatorioNfseDTO();
    }

    @Override
    public ConfiguracaoDeRelatorioNfseDTO insert(ConfiguracaoDeRelatorioNfseDTO dto) {
        return null;
    }

    @Override
    public ConfiguracaoDeRelatorioNfseDTO update(ConfiguracaoDeRelatorioNfseDTO dto) {
        return null;
    }

    @Override
    public void remove(ConfiguracaoDeRelatorioNfseDTO dto) {
    }
}
