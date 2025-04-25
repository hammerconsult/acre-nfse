package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegracaoNotaNacionalDTO extends AbstractEntity implements RowMapper<IntegracaoNotaNacionalDTO> {

    private String inscricaoCadastral;
    private Long idNotaFiscal;
    private String numeroNotaFiscal;
    private String status;
    private String chaveAcesso;
    private String mensagem;

    public String getInscricaoCadastral() {
        return inscricaoCadastral;
    }

    public void setInscricaoCadastral(String inscricaoCadastral) {
        this.inscricaoCadastral = inscricaoCadastral;
    }

    public Long getIdNotaFiscal() {
        return idNotaFiscal;
    }

    public void setIdNotaFiscal(Long idNotaFiscal) {
        this.idNotaFiscal = idNotaFiscal;
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public IntegracaoNotaNacionalDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        IntegracaoNotaNacionalDTO dto = new IntegracaoNotaNacionalDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setInscricaoCadastral(resultSet.getString("inscricao_cadastral"));
        dto.setIdNotaFiscal(resultSet.getLong("id_nota_fiscal"));
        dto.setNumeroNotaFiscal(resultSet.getString("numero_nota_fiscal"));
        dto.setStatus(resultSet.getString("status"));
        dto.setChaveAcesso(resultSet.getString("chave_acesso"));
        dto.setMensagem(resultSet.getString("mensagem"));
        return dto;
    }
}
