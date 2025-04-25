package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0400NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0400JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0400NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.codigodependencia, " +
                " obj.identificacaodependencia, " +
                " obj.cnpjproprio, " +
                " obj.tipodependencia_id, " +
                " obj.enderecodependencia, " +
                " obj.cnpjresponsavel, " +
                " obj.cidaderesponsavel_id, " +
                " obj.contabilidadepropria, " +
                " obj.datainicioparalizacao, " +
                " obj.datafimparalizacao ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0400 obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0400NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0400NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0400NfseDTO insert(ArquivoDesifRegistro0400NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0400 " +
                " (id, linha, arquivodesif_id, codigodependencia, identificacaodependencia, " +
                "  cnpjproprio, tipodependencia_id, enderecodependencia, cnpjresponsavel, " +
                "  cidaderesponsavel_id, contabilidadepropria, datainicioparalizacao, datafimparalizacao) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0400NfseDTO update(ArquivoDesifRegistro0400NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0400NfseDTO dto) {
        
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0400 where arquivodesif_id = ?",
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
