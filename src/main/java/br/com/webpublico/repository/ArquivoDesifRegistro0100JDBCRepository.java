package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0100NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0100JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0100NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.conta, " +
                " obj.desdobramento, " +
                " obj.nome, " +
                " obj.descricao, " +
                " obj.contasuperior, " +
                " obj.cosif_id, " +
                " obj.codigotributacao_id ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0100 obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0100NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0100NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0100NfseDTO insert(ArquivoDesifRegistro0100NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0100 " +
                " (id, linha, arquivodesif_id, conta, desdobramento, nome, descricao, " +
                " contasuperior, cosif_id, codigotributacao_id) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0100NfseDTO update(ArquivoDesifRegistro0100NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0100NfseDTO dto) {

    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0100 where arquivodesif_id = ?",
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
