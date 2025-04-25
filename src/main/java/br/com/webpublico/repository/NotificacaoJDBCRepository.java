package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.NotificacaoNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class NotificacaoJDBCRepository extends AbstractJDBCRepository<NotificacaoNfseDTO> {


    @Override
    public String getSelect() {
        return null;
    }

    @Override
    public String getFrom() {
        return null;
    }

    @Override
    public RowMapper<NotificacaoNfseDTO> newRowMapper() {
        return null;
    }

    @Override
    public NotificacaoNfseDTO insert(NotificacaoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("INSERT INTO NOTIFICACAO " +
                " (ID, TITULO, DESCRICAO, LINK, GRAVIDADE, VISUALIZADO, TIPONOTIFICACAO, " +
                " CRIADOEM) " +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public NotificacaoNfseDTO update(NotificacaoNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(NotificacaoNfseDTO dto) {
    }
}
