package br.com.webpublico.domain.search;

import br.com.webpublico.domain.dto.CnaeNfseDTO;
import br.com.webpublico.domain.dto.NfseDTO;

public class CnaeSearchDTO implements WPSearchDTO {

    private Long id;
    private String codigo;
    private String descricao;
    private Boolean ativo;

    public CnaeSearchDTO() {
    }

    public CnaeSearchDTO(CnaeNfseDTO cnae) {
        setId(cnae.getId());
        setCodigo(cnae.getCodigo());
        setDescricao(cnae.getDescricao());
        setAtivo(cnae.getAtivo());
    }

    @Override
    public NfseDTO toNfseDto() {
        CnaeNfseDTO cnaeDTO = new CnaeNfseDTO();
        cnaeDTO.setId(getId());
        cnaeDTO.setCodigo(getCodigo());
        cnaeDTO.setDescricao(getDescricao());
        cnaeDTO.setAtivo(getAtivo());
        return cnaeDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }


}
