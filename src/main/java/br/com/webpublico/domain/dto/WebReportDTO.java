package br.com.webpublico.domain.dto;

import br.com.webpublico.webreportdto.dto.comum.RelatorioDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WebReportDTO implements Serializable {

    public static final String HEADER_MESSAGE_ERROR = "webreport-error";

    private String nome;
    private String usuario;
    private Date solicitadoEm;
    private String uuid;
    private BigDecimal porcentagem;
    private byte[] conteudo;
    private Long inicio;
    private Long fim;
    private Boolean visualizado;
    private String erro;
    private String hash;
    private RelatorioDTO dto;


    public WebReportDTO() {
    }

    public WebReportDTO(String nome, String usuario, String uuid, String hash, RelatorioDTO dto) {
        this.nome = nome;
        this.usuario = usuario;
        this.uuid = uuid;
        this.solicitadoEm = new Date();
        this.porcentagem = BigDecimal.ZERO;
        this.inicio = System.currentTimeMillis();
        this.visualizado = Boolean.FALSE;
        this.hash = hash;
        this.dto = dto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getSolicitadoEm() {
        return solicitadoEm;
    }

    public void setSolicitadoEm(Date solicitadoEm) {
        this.solicitadoEm = solicitadoEm;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(BigDecimal porcentagem) {
        this.porcentagem = porcentagem;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    public Long getInicio() {
        return inicio;
    }

    public void setInicio(Long inicio) {
        this.inicio = inicio;
    }

    public Long getFim() {
        return fim;
    }

    public void setFim(Long fim) {
        this.fim = fim;
    }

    public Boolean getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(Boolean visualizado) {
        this.visualizado = visualizado;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public RelatorioDTO getDto() {
        return dto;
    }

    public void setDto(RelatorioDTO dto) {
        this.dto = dto;
    }
}
