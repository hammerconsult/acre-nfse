package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class ArquivoDesifRegistro0400NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0400NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private String codigoDependencia;
    private IdentificacaoDependenciaNfseDTO identificacaoDependencia;
    private String cnpjProprio;
    private TipoDependenciaDesifNfseDTO tipoDependencia;
    private String enderecoDependencia;
    private String cnpjResponsavel;
    private MunicipioNfseDTO municipioResponsavel;
    private Boolean contabilidadePropria;
    private Date dataInicioParalizacao;
    private Date dataFimParalizacao;

    public ArquivoDesifRegistro0400NfseDTO() {
        super();
        contabilidadePropria = Boolean.FALSE;
    }

    public Long getLinha() {
        return linha;
    }

    public void setLinha(Long linha) {
        this.linha = linha;
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
    }

    public String getCodigoDependencia() {
        return codigoDependencia;
    }

    public void setCodigoDependencia(String codigoDependencia) {
        this.codigoDependencia = codigoDependencia;
    }

    public IdentificacaoDependenciaNfseDTO getIdentificacaoDependencia() {
        return identificacaoDependencia;
    }

    public void setIdentificacaoDependencia(IdentificacaoDependenciaNfseDTO identificacaoDependencia) {
        this.identificacaoDependencia = identificacaoDependencia;
    }

    public String getCnpjProprio() {
        return cnpjProprio;
    }

    public void setCnpjProprio(String cnpjProprio) {
        this.cnpjProprio = cnpjProprio;
    }

    public TipoDependenciaDesifNfseDTO getTipoDependencia() {
        return tipoDependencia;
    }

    public void setTipoDependencia(TipoDependenciaDesifNfseDTO tipoDependencia) {
        this.tipoDependencia = tipoDependencia;
    }

    public String getEnderecoDependencia() {
        return enderecoDependencia;
    }

    public void setEnderecoDependencia(String enderecoDependencia) {
        this.enderecoDependencia = enderecoDependencia;
    }

    public String getCnpjResponsavel() {
        return cnpjResponsavel;
    }

    public void setCnpjResponsavel(String cnpjResponsavel) {
        this.cnpjResponsavel = cnpjResponsavel;
    }

    public MunicipioNfseDTO getMunicipioResponsavel() {
        return municipioResponsavel;
    }

    public void setMunicipioResponsavel(MunicipioNfseDTO municipioResponsavel) {
        this.municipioResponsavel = municipioResponsavel;
    }

    public Boolean getContabilidadePropria() {
        return contabilidadePropria;
    }

    public void setContabilidadePropria(Boolean contabilidadePropria) {
        this.contabilidadePropria = contabilidadePropria;
    }

    public Date getDataInicioParalizacao() {
        return dataInicioParalizacao;
    }

    public void setDataInicioParalizacao(Date dataInicioParalizacao) {
        this.dataInicioParalizacao = dataInicioParalizacao;
    }

    public Date getDataFimParalizacao() {
        return dataFimParalizacao;
    }

    public void setDataFimParalizacao(Date dataFimParalizacao) {
        this.dataFimParalizacao = dataFimParalizacao;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setString(4, codigoDependencia);
        if (identificacaoDependencia != null) {
            preparedStatement.setString(5, identificacaoDependencia.name());
        } else {
            preparedStatement.setNull(5, Types.NULL);
        }
        preparedStatement.setString(6, cnpjProprio);
        if (tipoDependencia != null) {
            preparedStatement.setLong(7, tipoDependencia.getId());
        } else {
            preparedStatement.setNull(7, Types.NULL);
        }
        preparedStatement.setString(8, enderecoDependencia);
        preparedStatement.setString(9, cnpjResponsavel);
        if (municipioResponsavel != null) {
            preparedStatement.setLong(10, municipioResponsavel.getId());
        } else {
            preparedStatement.setNull(10, Types.NULL);
        }
        preparedStatement.setBoolean(11, contabilidadePropria != null ? contabilidadePropria : Boolean.FALSE);
        if (dataInicioParalizacao != null) {
            preparedStatement.setDate(12, DateUtils.toSQLDate(dataInicioParalizacao));
        } else {
            preparedStatement.setNull(12, Types.NULL);
        }
        if (dataFimParalizacao != null) {
            preparedStatement.setDate(13, DateUtils.toSQLDate(dataFimParalizacao));
        } else {
            preparedStatement.setNull(13, Types.NULL);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0400NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0400NfseDTO dto = new ArquivoDesifRegistro0400NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setCodigoDependencia(resultSet.getString("codigodependencia"));
        dto.setIdentificacaoDependencia(IdentificacaoDependenciaNfseDTO.valueOf(resultSet.getString("identificacaodependencia")));
        dto.setCnpjProprio(resultSet.getString("cnpjproprio"));
        dto.setTipoDependencia(new TipoDependenciaDesifNfseDTO());
        dto.getTipoDependencia().setId(resultSet.getLong("tipodependencia_id"));
        dto.setEnderecoDependencia(resultSet.getString("enderecodependencia"));
        dto.setCnpjResponsavel(resultSet.getString("cnpjresponsavel"));
        dto.setMunicipioResponsavel(new MunicipioNfseDTO());
        dto.getMunicipioResponsavel().setId(resultSet.getLong("cidaderesponsavel_id"));
        dto.setContabilidadePropria(resultSet.getBoolean("contabilidadepropria"));
        dto.setDataInicioParalizacao(resultSet.getDate("datainicioparalizacao"));
        dto.setDataFimParalizacao(resultSet.getDate("datafimparalizacao"));
        return dto;
    }
}
