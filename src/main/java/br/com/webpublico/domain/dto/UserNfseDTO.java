package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNfseDTO extends AbstractEntity implements BatchPreparedStatementSetter {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int COMUM_FIELD = 255;

    private String login;

    private String nome;

    private String password;

    private String activationKey;

    private String resetKey;

    private String email;

    private List<String> roles;

    private boolean pessoaFisica;

    private PrestadorUsuarioNfseDTO empresa;

    private boolean activated;

    private Long pessoaId;

    private DadosPessoaisNfseDTO dadosPessoais;

    private boolean passwordTemporary;

    public UserNfseDTO() {
    }

    public UserNfseDTO(Long id, String login, String password, String nome, String email,
                       List<String> roles, boolean activated, boolean pessoaFisica, Long pessoaId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.roles = roles;
        this.activated = activated;
        this.pessoaFisica = pessoaFisica;
        this.pessoaId = pessoaId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PrestadorUsuarioNfseDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PrestadorUsuarioNfseDTO empresa) {
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(boolean pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public Long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public DadosPessoaisNfseDTO getDadosPessoais() {
        return dadosPessoais;
    }

    public void setDadosPessoais(DadosPessoaisNfseDTO dadosPessoais) {
        this.dadosPessoais = dadosPessoais;
    }

    public boolean isPasswordTemporary() {
        return passwordTemporary;
    }

    public void setPasswordTemporary(boolean passwordTemporary) {
        this.passwordTemporary = passwordTemporary;
    }

    @JsonIgnore
    public void validarDadosPessoaisUsuario() throws NfseOperacaoNaoPermitidaException {
        List<String> mensagens = Lists.newArrayList();

        if (Strings.isNullOrEmpty(getLogin())) {
            mensagens.add("O campo CPF ou CNPJ é obrigatório.");
        }
        if (Strings.isNullOrEmpty(getPassword())) {
            mensagens.add("O campo Senha é obrigatório!");
        }

        if (getDadosPessoais() == null) {
            mensagens.add("O campo Nome ou Razão Social é obrigatório.");
            mensagens.add("O campo E-mail é obrigatório.");
            mensagens.add("Informe ao menos um Telefone ou um Celular.");
            mensagens.add("O campo Cep é obrigatório.");
            mensagens.add("O campo Logradouro é obrigatório.");
            mensagens.add("O campo Número é obrigatório.");
            mensagens.add("O campo Bairro é obrigatório.");
            mensagens.add("O campo Número é obrigatório.");
            mensagens.add("O campo Município é obrigatório.");
            mensagens.add("O campo UF é obrigatório.");
        } else {
            if (Strings.isNullOrEmpty(getDadosPessoais().getNomeRazaoSocial())) {
                mensagens.add("O campo Nome ou Razão Social é obrigatório.");
            }
            if (Strings.isNullOrEmpty(getEmail())) {
                mensagens.add("O campo E-mail é obrigatório.");
            }
            if ((Strings.isNullOrEmpty(getDadosPessoais().getTelefone())) &&
                    (Strings.isNullOrEmpty(getDadosPessoais().getCelular()))) {
                mensagens.add("Informe ao menos um Telefone ou um Celular.");
            }
            if (Strings.isNullOrEmpty(getDadosPessoais().getCep())) {
                mensagens.add("O campo Cep é obrigatório.");
            }
            if (Strings.isNullOrEmpty(getDadosPessoais().getLogradouro())) {
                mensagens.add("O campo Logradouro é obrigatório.");
            }
            if (Strings.isNullOrEmpty(getDadosPessoais().getBairro())) {
                mensagens.add("O campo Bairro é obrigatório.");
            }
            if (Strings.isNullOrEmpty(getDadosPessoais().getNumero())) {
                mensagens.add("O campo Número é obrigatório.");
            }
            if (getDadosPessoais().getMunicipio() == null) {
                mensagens.add("O campo Município é obrigatório!");
            }
        }

        if (!mensagens.isEmpty()) {
            throw new NfseOperacaoNaoPermitidaException("Atenção!", mensagens);
        }
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, login);
        preparedStatement.setString(3, password);
        preparedStatement.setString(4, email);
        preparedStatement.setBoolean(5, activated);
        preparedStatement.setString(6, activationKey);
        if (pessoaId != null) {
            preparedStatement.setLong(7, pessoaId);
        } else {
            preparedStatement.setNull(7, Types.NULL);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
