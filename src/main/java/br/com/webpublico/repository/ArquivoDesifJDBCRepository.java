package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ArquivoDesifJDBCRepository extends AbstractJDBCRepository<ArquivoDesifNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.enviadopor_id, " +
                " obj.enviadoem, " +
                " obj.situacao, " +
                " obj.cadastroeconomico_id," +
                " obj.tipoinstituicaofinanceira_id, " +
                " obj.cidade_id," +
                " obj.iniciocompetencia, " +
                " obj.fimcompetencia, " +
                " obj.modulo, " +
                " obj.tipo, " +
                " obj.protocolo, " +
                " obj.tipoconsolidacao, " +
                " obj.cnpjresponsavel, " +
                " obj.versao, " +
                " obj.tipoarredondamento," +
                " dms.id as iddeclaracaomensalservico ";
    }

    @Override
    public String getFrom() {
        return "    from arquivodesif obj " +
                " left join declaracaomensalservico dms on dms.arquivodesif_id = obj.id ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<ArquivoDesifNfseDTO> newRowMapper() {
        return new ArquivoDesifNfseDTO();
    }

    @Override
    public ArquivoDesifNfseDTO insert(ArquivoDesifNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("insert into arquivodesif (id, enviadopor_id, enviadoem, situacao, cadastroeconomico_id, " +
                "                          tipoinstituicaofinanceira_id, cidade_id, iniciocompetencia, " +
                "                          fimcompetencia, modulo, tipo, protocolo, tipoconsolidacao, " +
                "                          cnpjresponsavel, versao, tipoarredondamento) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoDesifNfseDTO update(ArquivoDesifNfseDTO dto) {
        dto.setUpdate(Boolean.TRUE);
        jdbcTemplate.batchUpdate(" update arquivodesif set situacao = ?, protocolo = ? where id = ? ", dto);
        return dto;
    }

    @Override
    public void remove(ArquivoDesifNfseDTO dto) {
        jdbcTemplate.batchUpdate(" delete from arquivodesif where id = ? ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, dto.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }
}
