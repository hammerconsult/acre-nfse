package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.NfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.TipoDocumentoServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoServicoDeclaradoNfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * A DeclaracaoPrestacaoServico.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicoDeclaradoNfseDTO implements NfseDTO {

    private Long id;
    private PrestadorServicoNfseDTO cadastroEconomico;
    private Long numero;
    private Date emissao;
    private String emissaoString;
    private Long idDms;
    private TipoServicoDeclaradoNfseDTO tipoServicoDeclarado;
    private TipoDocumentoServicoDeclaradoNfseDTO tipoDocumentoServicoDeclarado;
    private DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrestadorServicoNfseDTO getCadastroEconomico() {
        return cadastroEconomico;
    }

    public void setCadastroEconomico(PrestadorServicoNfseDTO cadastroEconomico) {
        this.cadastroEconomico = cadastroEconomico;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public String getEmissaoString() {
        return emissaoString;
    }

    public void setEmissaoString(String emissaoString) {
        this.emissaoString = emissaoString;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public TipoServicoDeclaradoNfseDTO getTipoServicoDeclarado() {
        return tipoServicoDeclarado;
    }

    public void setTipoServicoDeclarado(TipoServicoDeclaradoNfseDTO tipoServicoDeclarado) {
        this.tipoServicoDeclarado = tipoServicoDeclarado;
    }

    public TipoDocumentoServicoDeclaradoNfseDTO getTipoDocumentoServicoDeclarado() {
        return tipoDocumentoServicoDeclarado;
    }

    public void setTipoDocumentoServicoDeclarado(TipoDocumentoServicoDeclaradoNfseDTO tipoDocumentoServicoDeclarado) {
        this.tipoDocumentoServicoDeclarado = tipoDocumentoServicoDeclarado;
    }

    public Long getIdDms() {
        return idDms;
    }

    public void setIdDms(Long idDms) {
        this.idDms = idDms;
    }

    public DeclaracaoPrestacaoServicoNfseDTO getDeclaracaoPrestacaoServico() {
        return declaracaoPrestacaoServico;
    }

    public void setDeclaracaoPrestacaoServico(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        this.declaracaoPrestacaoServico = declaracaoPrestacaoServico;
    }
}
