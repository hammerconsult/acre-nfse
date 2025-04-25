package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotaDeclaradaNfseDTO implements NfseDTO, BatchPreparedStatementSetter, RowMapper<NotaDeclaradaNfseDTO> {

    private DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico;
    private Long id;
    private Long idDeclaracaoMensalServico;

    public NotaDeclaradaNfseDTO() {
    }

    public DeclaracaoPrestacaoServicoNfseDTO getDeclaracaoPrestacaoServico() {
        return declaracaoPrestacaoServico;
    }

    public void setDeclaracaoPrestacaoServico(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        this.declaracaoPrestacaoServico = declaracaoPrestacaoServico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDeclaracaoMensalServico() {
        return idDeclaracaoMensalServico;
    }

    public void setIdDeclaracaoMensalServico(Long idDeclaracaoMensalServico) {
        this.idDeclaracaoMensalServico = idDeclaracaoMensalServico;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, this.getId());
        ps.setLong(2, this.getIdDeclaracaoMensalServico());
        ps.setLong(3, this.getDeclaracaoPrestacaoServico().getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public NotaDeclaradaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NotaDeclaradaNfseDTO dto = new NotaDeclaradaNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setIdDeclaracaoMensalServico(resultSet.getLong("declaracaomensalservico_id"));
        dto.setDeclaracaoPrestacaoServico(new DeclaracaoPrestacaoServicoNfseDTO());
        dto.getDeclaracaoPrestacaoServico().setId(resultSet.getLong("declaracaoprestacaoservico_id"));
        return dto;
    }
}
