package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class ArquivoDesifRegistro1000NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro1000NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private String cnpj;
    private MunicipioNfseDTO municipio;
    private String numeroLancamento;
    private Date dataLancamento;
    private BigDecimal valorPartidaLancamento;
    private String conta;
    private TipoPartidaNfseDTO tipoPartida;
    private EventoContabilDesifNfseDTO eventoContabil;
    private MunicipioNfseDTO municipioVinculo;
    private String historico;

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public MunicipioNfseDTO getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioNfseDTO municipio) {
        this.municipio = municipio;
    }

    public String getNumeroLancamento() {
        return numeroLancamento;
    }

    public void setNumeroLancamento(String numeroLancamento) {
        this.numeroLancamento = numeroLancamento;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public BigDecimal getValorPartidaLancamento() {
        return valorPartidaLancamento;
    }

    public void setValorPartidaLancamento(BigDecimal valorPartidaLancamento) {
        this.valorPartidaLancamento = valorPartidaLancamento;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public TipoPartidaNfseDTO getTipoPartida() {
        return tipoPartida;
    }

    public void setTipoPartida(TipoPartidaNfseDTO tipoPartida) {
        this.tipoPartida = tipoPartida;
    }

    public EventoContabilDesifNfseDTO getEventoContabil() {
        return eventoContabil;
    }

    public void setEventoContabil(EventoContabilDesifNfseDTO eventoContabil) {
        this.eventoContabil = eventoContabil;
    }

    public MunicipioNfseDTO getMunicipioVinculo() {
        return municipioVinculo;
    }

    public void setMunicipioVinculo(MunicipioNfseDTO municipioVinculo) {
        this.municipioVinculo = municipioVinculo;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setString(4, cnpj);
        if (municipio != null) {
            preparedStatement.setLong(5, municipio.getId());
        } else {
            preparedStatement.setNull(5, Types.NULL);
        }
        preparedStatement.setString(6, numeroLancamento);
        preparedStatement.setDate(7, DateUtils.toSQLDate(dataLancamento));
        preparedStatement.setBigDecimal(8, valorPartidaLancamento);
        preparedStatement.setString(9, conta);
        if (tipoPartida != null) {
            preparedStatement.setString(10, tipoPartida.name());
        } else {
            preparedStatement.setNull(10, Types.NULL);
        }
        if (eventoContabil != null) {
            preparedStatement.setLong(11, eventoContabil.getId());
        } else {
            preparedStatement.setNull(11, Types.NULL);
        }
        if (municipioVinculo != null) {
            preparedStatement.setLong(12, municipioVinculo.getId());
        } else {
            preparedStatement.setNull(12, Types.NULL);
        }
        preparedStatement.setString(13, historico);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro1000NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro1000NfseDTO dto = new ArquivoDesifRegistro1000NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setCnpj(resultSet.getString("cnpj"));
        if (resultSet.getLong("cidade_id") != 0) {
            dto.setMunicipio(new MunicipioNfseDTO());
            dto.getMunicipio().setId(resultSet.getLong("cidade_id"));
        }
        dto.setNumeroLancamento(resultSet.getString("numerolancamento"));
        dto.setDataLancamento(resultSet.getDate("datalancamento"));
        dto.setValorPartidaLancamento(resultSet.getBigDecimal("valorpartidalancamento"));
        dto.setConta(resultSet.getString("conta"));
        if (!Strings.isNullOrEmpty(resultSet.getString("tipopartida"))) {
            dto.setTipoPartida(TipoPartidaNfseDTO.valueOf(resultSet.getString("tipopartida")));
        }
        if (resultSet.getLong("eventocontabil_id") != 0) {
            dto.setEventoContabil(new EventoContabilDesifNfseDTO());
            dto.getEventoContabil().setId(resultSet.getLong("eventocontabil_id"));
        }
        if (resultSet.getLong("cidadevinculo_id") != 0) {
            dto.setMunicipioVinculo(new MunicipioNfseDTO());
            dto.getMunicipioVinculo().setId(resultSet.getLong("cidadevinculo_id"));
        }
        dto.setHistorico(resultSet.getString("historico"));
        return dto;
    }
}
