package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0410NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0410JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0410NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, obj.linha, obj.arquivodesif_id, obj.codigodependencia, " +
                " obj.competencia, obj.conta_id, obj.saldoinicial, obj.valordebito, " +
                " obj.valorcredito, obj.saldofinal ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0410 obj ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0410NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0410NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0410NfseDTO insert(ArquivoDesifRegistro0410NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0410 " +
                " (id, linha, arquivodesif_id, codigodependencia, competencia, conta_id, " +
                " saldoinicial, valordebito, valorcredito, saldofinal) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0410NfseDTO update(ArquivoDesifRegistro0410NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0410NfseDTO dto) {
        
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0410 where arquivodesif_id = ?",
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
