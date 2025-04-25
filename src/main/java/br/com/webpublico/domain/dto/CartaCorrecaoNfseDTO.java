package br.com.webpublico.domain.dto;

import br.com.webpublico.DateUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class CartaCorrecaoNfseDTO implements Serializable, NfseDTO, BatchPreparedStatementSetter, RowMapper<CartaCorrecaoNfseDTO> {

    private Long id;
    private Date dataEmissao;
    private Long idNotaFiscal;
    private Long sequencialCartaCorrecao;
    private String codigoVerificacao;
    private String descricaoAlteracao;
    private TomadorServicoDTO tomadorServicoNfse;
    private PrestadorServicoNfseDTO prestadorServicoNfseDTO;

    public CartaCorrecaoNfseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Long getIdNotaFiscal() {
        return idNotaFiscal;
    }

    public void setIdNotaFiscal(Long idNotaFiscal) {
        this.idNotaFiscal = idNotaFiscal;
    }

    public Long getSequencialCartaCorrecao() {
        return sequencialCartaCorrecao;
    }

    public void setSequencialCartaCorrecao(Long sequencialCartaCorrecao) {
        this.sequencialCartaCorrecao = sequencialCartaCorrecao;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public String getDescricaoAlteracao() {
        return descricaoAlteracao;
    }

    public void setDescricaoAlteracao(String descricaoAlteracao) {
        this.descricaoAlteracao = descricaoAlteracao;
    }

    public TomadorServicoDTO getTomadorServicoNfse() {
        return tomadorServicoNfse;
    }

    public void setTomadorServicoNfse(TomadorServicoDTO tomadorServicoNfse) {
        this.tomadorServicoNfse = tomadorServicoNfse;
    }

    public PrestadorServicoNfseDTO getPrestadorServicoNfseDTO() {
        return prestadorServicoNfseDTO;
    }

    public void setPrestadorServicoNfseDTO(PrestadorServicoNfseDTO prestadorServicoNfseDTO) {
        this.prestadorServicoNfseDTO = prestadorServicoNfseDTO;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, id);
        ps.setDate(2, DateUtils.toSQLDate(dataEmissao));
        ps.setLong(3, idNotaFiscal);
        ps.setLong(4, sequencialCartaCorrecao);
        ps.setString(5, codigoVerificacao);
        ps.setString(6, descricaoAlteracao);
        if (tomadorServicoNfse != null) {
            ps.setLong(7, tomadorServicoNfse.getId());
        } else {
            ps.setNull(7, Types.NULL);
        }
        ps.setLong(8, prestadorServicoNfseDTO.getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public CartaCorrecaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        CartaCorrecaoNfseDTO dto = new CartaCorrecaoNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setDataEmissao(resultSet.getDate("dataemissao"));
        dto.setIdNotaFiscal(resultSet.getLong("notafiscal_id"));
        dto.setSequencialCartaCorrecao(resultSet.getLong("sequencialcartacorrecao"));
        dto.setCodigoVerificacao(resultSet.getString("codigoverificacao"));
        dto.setDescricaoAlteracao(resultSet.getString("descricaoalteracao"));
        if (resultSet.getLong("tomadorserviconfse_id") != 0) {
            dto.setTomadorServicoNfse(new TomadorServicoDTO());
            dto.getTomadorServicoNfse().setId(resultSet.getLong("tomadorserviconfse_id"));
        }
        dto.setPrestadorServicoNfseDTO(new PrestadorServicoNfseDTO());
        dto.getPrestadorServicoNfseDTO().setId(resultSet.getLong("prestador_id"));
        return dto;
    }
}
