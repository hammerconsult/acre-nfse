package br.com.webpublico.domain.dto;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by rodolfo on 31/10/17.
 */
public class PrestadorUsuarioNfseDTO {

    private Long id;
    private Long idUsuario;
    private String login;
    private String nome;
    private String email;
    private String funcao;
    private boolean permitido;
    private boolean contador;
    private boolean bloqueadoEmissaoNfse;
    private String telefoneDesbloqueio;
    private PrestadorServicoNfseDTO prestador;
    private List<String> roles;
    private boolean passwordTemporary;

    public PrestadorUsuarioNfseDTO() {
        roles = Lists.newArrayList();
    }

    public PrestadorUsuarioNfseDTO(boolean permitido, PrestadorServicoNfseDTO prestador) {
        this();
        this.permitido = permitido;
        this.prestador = prestador;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isPermitido() {
        return permitido;
    }

    public void setPermitido(boolean permitido) {
        this.permitido = permitido;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isContador() {
        return contador;
    }

    public void setContador(boolean contador) {
        this.contador = contador;
    }

    public boolean isBloqueadoEmissaoNfse() {
        return bloqueadoEmissaoNfse;
    }

    public void setBloqueadoEmissaoNfse(boolean bloqueadoEmissaoNfse) {
        this.bloqueadoEmissaoNfse = bloqueadoEmissaoNfse;
    }

    public String getTelefoneDesbloqueio() {
        return telefoneDesbloqueio;
    }

    public void setTelefoneDesbloqueio(String telefoneDesbloqueio) {
        this.telefoneDesbloqueio = telefoneDesbloqueio;
    }

    public boolean isPasswordTemporary() {
        return passwordTemporary;
    }

    public void setPasswordTemporary(boolean passwordTemporary) {
        this.passwordTemporary = passwordTemporary;
    }
}
