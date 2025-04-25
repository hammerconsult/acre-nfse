package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0430NfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifRegistro0430JDBCRepository extends AbstractJDBCRepository<ArquivoDesifRegistro0430NfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.linha, " +
                " obj.arquivodesif_id, " +
                " obj.codigodependencia, " +
                " obj.conta_id, " +
                " obj.codigotributacao_id, " +
                " obj.valorcredito, " +
                " obj.valordebito, " +
                " obj.valorreceitatributavel, " +
                " obj.valordeducaolegal, " +
                " obj.discriminacaodeducao, " +
                " obj.basecalculo, " +
                " obj.aliquota, " +
                " obj.valorincentivofiscal, " +
                " obj.discriminacaoincentivo, " +
                " obj.valorissqnretido, " +
                " obj.exigibilidadesuspensao, " +
                " obj.processosuspensao ";
    }

    @Override
    public String getFrom() {
        return " from arquivodesifregistro0430 obj ";
    }

    @Override
    public RowMapper<ArquivoDesifRegistro0430NfseDTO> newRowMapper() {
        return new ArquivoDesifRegistro0430NfseDTO();
    }

    @Override
    public ArquivoDesifRegistro0430NfseDTO insert(ArquivoDesifRegistro0430NfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivodesifregistro0430 " +
                " (id, linha, arquivodesif_id, codigodependencia, conta_id, codigotributacao_id, " +
                " valorcredito, valordebito, valorreceitatributavel, valordeducaolegal, " +
                " discriminacaodeducao, basecalculo, aliquota, valorincentivofiscal, " +
                " discriminacaoincentivo, valorissqnretido, exigibilidadesuspensao, processosuspensao) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifRegistro0430NfseDTO update(ArquivoDesifRegistro0430NfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoDesifRegistro0430NfseDTO dto) {
        
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        jdbcTemplate.batchUpdate(" delete from arquivodesifregistro0430 where arquivodesif_id = ? ",
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
