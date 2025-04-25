package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro1000NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro1000JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro1000NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.cnpj, " +
                " obj.cidade_id, " +
                " obj.numerolancamento, " +
                " obj.datalancamento, " +
                " obj.valorpartidalancamento, " +
                " obj.conta, " +
                " obj.tipopartida, " +
                " obj.eventocontabil_id, " +
                " obj.cidadevinculo_id, " +
                " obj.historico ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro1000 obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro1000NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro1000NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro1000NfseDTO insert(ArquivoDesifRegistro1000NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro1000 (id, linha, arquivodesif_id, " +
                " cnpj, cidade_id, numerolancamento, datalancamento, valorpartidalancamento, conta, tipopartida, " +
                " eventocontabil_id, cidadevinculo_id, historico) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro1000NfseDTO update(ArquivoDesifRegistro1000NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro1000NfseDTO dto) {

    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro1000 where arquivodesif_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, idArquivo);
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }
}
