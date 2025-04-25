package br.com.webpublico.domain.search;

import br.com.webpublico.domain.dto.NfseDTO;
import br.com.webpublico.domain.dto.PaisNfseDTO;

public class PaisSearchDTO implements WPSearchDTO {

    private Long id;
    private String codigo;
    private String nome;
    private String sigla;

    public PaisSearchDTO() {
    }

    public PaisSearchDTO(PaisNfseDTO pais) {
        setId(pais.getId());
        setCodigo(pais.getCodigo());
        setNome(pais.getNome());
        setSigla(pais.getSigla());
    }

    @Override
    public NfseDTO toNfseDto() {
        PaisNfseDTO paisDto = new PaisNfseDTO();
        paisDto.setId(getId());
        paisDto.setCodigo(getCodigo());
        paisDto.setNome(getNome());
        paisDto.setSigla(getSigla());
        return paisDto;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

}
