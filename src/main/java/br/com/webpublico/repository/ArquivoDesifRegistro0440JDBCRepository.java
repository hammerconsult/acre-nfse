package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0440NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0440JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0440NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.cnpjdependencia, " +
                " obj.codigotributacao_id, " +
                " obj.valorreceitatributavel, " +
                " obj.valordeducaoconta, " +
                " obj.valordeducaoconsolidado, " +
                " obj.discriminacaodeducao, " +
                " obj.basecalculo, " +
                " obj.aliquota, " +
                " obj.valorissqn, " +
                " obj.valorissqnretido, " +
                " obj.valorincentivoconta, " +
                " obj.valorincentivoconsolidado, " +
                " obj.discriminacaoincentivo, " +
                " obj.valorcompensacao, " +
                " obj.discriminacaocompensacao, " +
                " obj.valorissqnrecolhido, " +
                " obj.exigibilidadesuspensao, " +
                " obj.processosuspensao, " +
                " obj.valorissqnrecolher ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0440 obj ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0440NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0440NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0440NfseDTO insert(ArquivoDesifRegistro0440NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0440 " +
                " (id, linha, arquivodesif_id, cnpjdependencia, codigotributacao_id, valorreceitatributavel, " +
                " valordeducaoconta, valordeducaoconsolidado, discriminacaodeducao, basecalculo, aliquota, " +
                " valorissqn, valorissqnretido, valorincentivoconta, valorincentivoconsolidado, discriminacaoincentivo, " +
                " valorcompensacao, discriminacaocompensacao, valorissqnrecolhido, exigibilidadesuspensao, " +
                " processosuspensao, valorissqnrecolher) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0440NfseDTO update(ArquivoDesifRegistro0440NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0440NfseDTO dto) {

    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0440 where arquivodesif_id = ?",
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
