package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicoNfseDTO extends AbstractEntity implements RowMapper<ServicoNfseDTO> {

    private String codigo;
    private String descricao;
    private BigDecimal aliquota;
    private BigDecimal aliquotaIBPT;
    private Boolean construcaoCivil;
    private Boolean permiteRecolhimentoFora;
    private Boolean permiteDeducao;
    private BigDecimal percentualDeducao;
    private ExigibilidadeNfseDTO exigibilidade;
    private Boolean exclusivoSimplesNacional;
    private Boolean vetadoLC1162003;
    private Boolean permiteExportacao;
    private AnexoLei1232006NfseDTO anexoLei1232006;
    private Boolean permiteAlterarAnexoLei1232006;

    public ServicoNfseDTO() {
    }

    public ServicoNfseDTO(Long id) {
        this.id = id;
    }

    public ServicoNfseDTO(Long id, String codigo, String descricao, BigDecimal aliquota, BigDecimal aliquotaIBPT) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.aliquota = aliquota;
        this.aliquotaIBPT = aliquotaIBPT;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getAliquotaIBPT() {
        return aliquotaIBPT;
    }

    public void setAliquotaIBPT(BigDecimal aliquotaIBPT) {
        this.aliquotaIBPT = aliquotaIBPT;
    }

    public Boolean getConstrucaoCivil() {
        return construcaoCivil;
    }

    public void setConstrucaoCivil(Boolean construcaoCivil) {
        this.construcaoCivil = construcaoCivil;
    }

    public Boolean getPermiteRecolhimentoFora() {
        return permiteRecolhimentoFora;
    }

    public void setPermiteRecolhimentoFora(Boolean permiteRecolhimentoFora) {
        this.permiteRecolhimentoFora = permiteRecolhimentoFora;
    }

    public Boolean getPermiteDeducao() {
        return permiteDeducao;
    }

    public void setPermiteDeducao(Boolean permiteDeducao) {
        this.permiteDeducao = permiteDeducao;
    }

    public BigDecimal getPercentualDeducao() {
        return percentualDeducao;
    }

    public void setPercentualDeducao(BigDecimal percentualDeducao) {
        this.percentualDeducao = percentualDeducao;
    }

    public ExigibilidadeNfseDTO getExigibilidade() {
        return exigibilidade;
    }

    public void setExigibilidade(ExigibilidadeNfseDTO exigibilidadeNfseDTO) {
        this.exigibilidade = exigibilidadeNfseDTO;
    }

    public Boolean getExclusivoSimplesNacional() {
        return exclusivoSimplesNacional;
    }

    public void setExclusivoSimplesNacional(Boolean exclusivoSimplesNacional) {
        this.exclusivoSimplesNacional = exclusivoSimplesNacional;
    }

    public Boolean getVetadoLC1162003() {
        return vetadoLC1162003;
    }

    public void setVetadoLC1162003(Boolean vetadoLC1162003) {
        this.vetadoLC1162003 = vetadoLC1162003;
    }

    public Boolean getPermiteExportacao() {
        return permiteExportacao;
    }

    public void setPermiteExportacao(Boolean permiteExportacao) {
        this.permiteExportacao = permiteExportacao;
    }

    public AnexoLei1232006NfseDTO getAnexoLei1232006() {
        return anexoLei1232006;
    }

    public void setAnexoLei1232006(AnexoLei1232006NfseDTO anexoLei1232006) {
        this.anexoLei1232006 = anexoLei1232006;
    }

    public Boolean getPermiteAlterarAnexoLei1232006() {
        return permiteAlterarAnexoLei1232006;
    }

    public void setPermiteAlterarAnexoLei1232006(Boolean permiteAlterarAnexoLei1232006) {
        this.permiteAlterarAnexoLei1232006 = permiteAlterarAnexoLei1232006;
    }

    @Override
    public String toString() {
        return "ServicoSearchDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", aliquota=" + aliquota +
                ", aliquotaIBPT=" + aliquotaIBPT +
                '}';
    }

    @Override
    public ServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ServicoNfseDTO dto = new ServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCodigo(resultSet.getString("CODIGO"));
        dto.setDescricao(resultSet.getString("NOME"));
        dto.setAliquota(resultSet.getBigDecimal("ALIQUOTAISSHOMOLOGADO"));
        dto.setConstrucaoCivil(resultSet.getBoolean("CONSTRUCAOCIVIL"));
        dto.setPermiteRecolhimentoFora(resultSet.getBoolean("PERMITERECOLHIMENTOFORA"));
        dto.setPermiteDeducao(resultSet.getBoolean("PERMITEDEDUCAO"));
        dto.setPercentualDeducao(resultSet.getBigDecimal("PERCENTUALDEDUCAO"));
        dto.setExclusivoSimplesNacional(resultSet.getBoolean("EXCLUSIVOSIMPLESNACIONAL"));
        dto.setVetadoLC1162003(resultSet.getBoolean("VETADOLC1162003"));
        if (resultSet.getLong("ANEXOLEI1232006_ID") > 0) {
           dto.setAnexoLei1232006(new AnexoLei1232006NfseDTO());
           dto.getAnexoLei1232006().setId(resultSet.getLong("ANEXOLEI1232006_ID"));
        }
        dto.setPermiteAlterarAnexoLei1232006(resultSet.getBoolean("PERMITEALTERARANEXOLEI1232006"));
        return dto;
    }
}
