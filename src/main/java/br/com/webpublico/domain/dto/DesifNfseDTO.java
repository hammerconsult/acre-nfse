package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DesifNfseDTO implements Serializable, BatchPreparedStatementSetter {

    private Long id;
    private PrestadorServicoNfseDTO prestador;
    private DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public DeclaracaoPrestacaoServicoNfseDTO getDeclaracaoPrestacaoServico() {
        return declaracaoPrestacaoServico;
    }

    public void setDeclaracaoPrestacaoServico(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        this.declaracaoPrestacaoServico = declaracaoPrestacaoServico;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, getId());
        ps.setLong(2, getPrestador().getId());
        ps.setLong(3, getDeclaracaoPrestacaoServico().getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
