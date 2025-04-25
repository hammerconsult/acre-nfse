package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.concurrent.atomic.AtomicInteger;

public class DadosPessoaisSetter implements BatchPreparedStatementSetter {

    private DadosPessoaisNfseDTO dto;
    private boolean update;

    public DadosPessoaisSetter(DadosPessoaisNfseDTO dto, boolean update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        AtomicInteger index = new AtomicInteger(0);
        if (!update) {
            ps.setLong(index.incrementAndGet(), dto.getId());
            ps.setString(index.incrementAndGet(), dto.getInscricaoMunicipal());
            ps.setString(index.incrementAndGet(), dto.getInscricaoEstadualRg());
            ps.setString(index.incrementAndGet(), dto.getNomeRazaoSocial());
            ps.setString(index.incrementAndGet(), dto.getNomeFantasia());
            ps.setString(index.incrementAndGet(), dto.getEmail());
            ps.setString(index.incrementAndGet(), dto.getTelefone());
            ps.setString(index.incrementAndGet(), dto.getCelular());
            ps.setString(index.incrementAndGet(), dto.getCep());
            ps.setString(index.incrementAndGet(), dto.getNumero());
            ps.setString(index.incrementAndGet(), dto.getLogradouro());
            ps.setString(index.incrementAndGet(), dto.getBairro());
            ps.setString(index.incrementAndGet(), dto.getComplemento());
            if (dto.getMunicipio() != null) {
                ps.setString(index.incrementAndGet(), dto.getMunicipio().getNome());
                ps.setString(index.incrementAndGet(), dto.getMunicipio().getEstado());
                ps.setString(index.incrementAndGet(), dto.getMunicipio().getCodigoIbge());
            } else {
                ps.setNull(index.incrementAndGet(), Types.NULL);
                ps.setNull(index.incrementAndGet(), Types.NULL);
                ps.setNull(index.incrementAndGet(), Types.NULL);
            }
            if (dto.getPais() != null) {
                ps.setString(index.incrementAndGet(), dto.getPais().getNome());
                ps.setString(index.incrementAndGet(), dto.getPais().getCodigo());
            } else {
                ps.setNull(index.incrementAndGet(), Types.NULL);
                ps.setNull(index.incrementAndGet(), Types.NULL);
            }
            ps.setString(index.incrementAndGet(), dto.getApelido());
            ps.setString(index.incrementAndGet(), dto.getNumeroIdentificacao());
            ps.setString(index.incrementAndGet(), dto.getCpfCnpj());
            if (dto.getTipoIssqn() != null) {
                ps.setString(index.incrementAndGet(), dto.getTipoIssqn().name());
            } else {
                ps.setNull(index.incrementAndGet(), Types.NULL);
            }
            if (dto.getRegimeTributario() != null) {
                ps.setString(index.incrementAndGet(), dto.getRegimeTributario().name());
            } else {
                ps.setNull(index.incrementAndGet(), Types.NULL);
            }
        } else {
            ps.setString(index.incrementAndGet(), dto.getInscricaoMunicipal());
            ps.setString(index.incrementAndGet(), dto.getInscricaoEstadualRg());
            ps.setString(index.incrementAndGet(), dto.getNomeRazaoSocial());
            ps.setString(index.incrementAndGet(), dto.getNomeFantasia());
            ps.setString(index.incrementAndGet(), dto.getEmail());
            ps.setString(index.incrementAndGet(), dto.getTelefone());
            ps.setString(index.incrementAndGet(), dto.getCelular());
            ps.setString(index.incrementAndGet(), dto.getCep());
            ps.setString(index.incrementAndGet(), dto.getNumero());
            ps.setString(index.incrementAndGet(), dto.getLogradouro());
            ps.setString(index.incrementAndGet(), dto.getBairro());
            ps.setString(index.incrementAndGet(), dto.getComplemento());
            if (dto.getMunicipio() != null) {
                ps.setString(index.incrementAndGet(), dto.getMunicipio().getNome());
                ps.setString(index.incrementAndGet(), dto.getMunicipio().getEstado());
                ps.setString(index.incrementAndGet(), dto.getMunicipio().getCodigoIbge());
            } else {
                ps.setNull(index.incrementAndGet(), Types.NULL);
                ps.setNull(index.incrementAndGet(), Types.NULL);
                ps.setNull(index.incrementAndGet(), Types.NULL);
            }
            if (dto.getPais() != null) {
                ps.setString(index.incrementAndGet(), dto.getPais().getNome());
                ps.setString(index.incrementAndGet(), dto.getPais().getCodigo());
            } else {
                ps.setNull(index.incrementAndGet(), Types.NULL);
                ps.setNull(index.incrementAndGet(), Types.NULL);
            }
            ps.setString(index.incrementAndGet(), dto.getApelido());
            ps.setString(index.incrementAndGet(), dto.getNumeroIdentificacao());
            ps.setString(index.incrementAndGet(), dto.getCpfCnpj());
            ps.setLong(index.incrementAndGet(), dto.getId());
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
