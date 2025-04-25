package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0300NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0300JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0300NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.produtoservicobancario_id, " +
                " obj.descricaocomplementar, " +
                " obj.conta, " +
                " obj.desdobramento ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0300 obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0300NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0300NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0300NfseDTO insert(ArquivoDesifRegistro0300NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0300 " +
                " (id, linha, arquivodesif_id, produtoservicobancario_id, descricaocomplementar, conta, desdobramento) " +
                " values (?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0300NfseDTO update(ArquivoDesifRegistro0300NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0300NfseDTO dto) {
        
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0300 where arquivodesif_id = ?",
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
