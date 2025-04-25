package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0200NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0200JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0200NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.tarifabancaria_id, " +
                " obj.iniciovigencia, " +
                " obj.valorunitario, " +
                " obj.valorpercentual, " +
                " obj.conta, " +
                " obj.desdobramento ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0200 obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0200NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0200NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0200NfseDTO insert(ArquivoDesifRegistro0200NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0200 " +
                " (id, linha, arquivodesif_id, tarifabancaria_id, iniciovigencia, " +
                " valorunitario, valorpercentual, conta, desdobramento) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0200NfseDTO update(ArquivoDesifRegistro0200NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0200NfseDTO dto) {
        
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0200 where arquivodesif_id = ?",
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
