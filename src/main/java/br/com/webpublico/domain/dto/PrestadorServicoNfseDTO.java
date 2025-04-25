package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.SituacaoCadastralNfseDTO;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrestadorServicoNfseDTO implements NfseDTO {

    private Long id;
    private String inscricaoMunicipal;
    private PessoaNfseDTO pessoa;
    private List<SocioNfseDTO> socios = Lists.newArrayList();
    private List<ServicoNfseDTO> servicos = Lists.newArrayList();
    private List<CnaeNfseDTO> cnaes = Lists.newArrayList();
    private List<UserNfseDTO> usuarios = Lists.newArrayList();
    private EnquadramentoFiscalNfseDTO enquadramentoFiscal;
    private ArquivoDTO imagem;
    private Boolean emiteNfs;
    private Boolean enviaEmailNfseTomador;
    private Boolean enviaEmailNfseSocios;
    private Boolean enviaEmailNfseContador;
    private Boolean enviaEmailCancelaNfseTomador;
    private Boolean enviaEmailCancelaNfseSocios;
    private Boolean enviaEmailCancelaNfseContador;
    private String nomeParaContato;
    private String telefoneParaContato;
    private String resumoSobreEmpresa;
    private NaturezaJuridicaNfseDTO naturezaJuridica;
    private List<ReceitaTributariaBrutaNfseDTO> receitasTributariaBruta;
    private List<ReceitaTributariaBrutaNfseDTO> ultimasReceitasTributariaBruta;
    private Date dataCredenciamentoISSOnline;
    private String chaveAcesso;
    private Boolean criarUsuario;
    private String password;
    private SituacaoCadastralNfseDTO situacao;
    private BancoNfseDTO banco;
    private Date abertura;
    private Date encerramento;

    public PrestadorServicoNfseDTO() {
        criarUsuario = Boolean.FALSE;
    }

    public PrestadorServicoNfseDTO(Long id, String inscricaoMunicipal, String nome, String nomeFantasianomeFantasia, String cpfCnpj) {
        this();
        this.id = id;
        this.inscricaoMunicipal = inscricaoMunicipal;
        this.pessoa = new PessoaNfseDTO(null, new DadosPessoaisNfseDTO());
        this.pessoa.getDadosPessoais().setCpfCnpj(cpfCnpj);
        this.pessoa.getDadosPessoais().setNomeRazaoSocial(nome);
        this.pessoa.getDadosPessoais().setNomeFantasia(nomeFantasianomeFantasia);
    }

    public PrestadorServicoNfseDTO(Long id, String inscricaoMunicipal, String nome,
                                   String nomeFantasianomeFantasia, String cpfCnpj, EnquadramentoFiscalNfseDTO enquadramentoFiscal) {
        this(id, inscricaoMunicipal, nome, nomeFantasianomeFantasia, cpfCnpj);
        this.enquadramentoFiscal = enquadramentoFiscal;
    }

    public PrestadorServicoNfseDTO(Long id, String inscricaoMunicipal, PessoaNfseDTO pessoa) {
        this.id = id;
        this.inscricaoMunicipal = inscricaoMunicipal;
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public PessoaNfseDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaNfseDTO pessoa) {
        this.pessoa = pessoa;
    }

    public List<SocioNfseDTO> getSocios() {
        return socios;
    }

    public void setSocios(List<SocioNfseDTO> socios) {
        this.socios = socios;
    }

    public List<ServicoNfseDTO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoNfseDTO> servicos) {
        this.servicos = servicos;
    }

    public List<CnaeNfseDTO> getCnaes() {
        return cnaes;
    }

    public void setCnaes(List<CnaeNfseDTO> cnaes) {
        this.cnaes = cnaes;
    }

    public EnquadramentoFiscalNfseDTO getEnquadramentoFiscal() {
        return enquadramentoFiscal;
    }

    public void setEnquadramentoFiscal(EnquadramentoFiscalNfseDTO enquadramentoFiscal) {
        this.enquadramentoFiscal = enquadramentoFiscal;
    }

    public Boolean getEmiteNfs() {
        return emiteNfs;
    }

    public void setEmiteNfs(Boolean emiteNfs) {
        this.emiteNfs = emiteNfs;
    }

    public ArquivoDTO getImagem() {
        return imagem;
    }

    public void setImagem(ArquivoDTO imagem) {
        this.imagem = imagem;
    }

    public Boolean getEnviaEmailNfseTomador() {
        return enviaEmailNfseTomador;
    }

    public void setEnviaEmailNfseTomador(Boolean enviaEmailNfseTomador) {
        this.enviaEmailNfseTomador = enviaEmailNfseTomador;
    }

    public Boolean getEnviaEmailNfseContador() {
        return enviaEmailNfseContador;
    }

    public void setEnviaEmailNfseContador(Boolean enviaEmailNfseContador) {
        this.enviaEmailNfseContador = enviaEmailNfseContador;
    }

    public Boolean getEnviaEmailCancelaNfseTomador() {
        return enviaEmailCancelaNfseTomador;
    }

    public void setEnviaEmailCancelaNfseTomador(Boolean enviaEmailCancelaNfseTomador) {
        this.enviaEmailCancelaNfseTomador = enviaEmailCancelaNfseTomador;
    }

    public Boolean getEnviaEmailCancelaNfseContador() {
        return enviaEmailCancelaNfseContador;
    }

    public void setEnviaEmailCancelaNfseContador(Boolean enviaEmailCancelaNfseContador) {
        this.enviaEmailCancelaNfseContador = enviaEmailCancelaNfseContador;
    }

    public String getNomeParaContato() {
        return nomeParaContato;
    }

    public void setNomeParaContato(String nomeParaContato) {
        this.nomeParaContato = nomeParaContato;
    }

    public String getTelefoneParaContato() {
        return telefoneParaContato;
    }

    public void setTelefoneParaContato(String telefoneParaContato) {
        this.telefoneParaContato = telefoneParaContato;
    }

    public String getResumoSobreEmpresa() {
        return resumoSobreEmpresa;
    }

    public void setResumoSobreEmpresa(String resumoSobreEmpresa) {
        this.resumoSobreEmpresa = resumoSobreEmpresa;
    }

    public List<UserNfseDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UserNfseDTO> usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getEnviaEmailNfseSocios() {
        return enviaEmailNfseSocios;
    }

    public void setEnviaEmailNfseSocios(Boolean enviaEmailNfseSocios) {
        this.enviaEmailNfseSocios = enviaEmailNfseSocios;
    }

    public Boolean getEnviaEmailCancelaNfseSocios() {
        return enviaEmailCancelaNfseSocios;
    }

    public void setEnviaEmailCancelaNfseSocios(Boolean enviaEmailCancelaNfseSocios) {
        this.enviaEmailCancelaNfseSocios = enviaEmailCancelaNfseSocios;
    }

    public NaturezaJuridicaNfseDTO getNaturezaJuridica() {
        return naturezaJuridica;
    }

    public void setNaturezaJuridica(NaturezaJuridicaNfseDTO naturezaJuridica) {
        this.naturezaJuridica = naturezaJuridica;
    }

    public List<ReceitaTributariaBrutaNfseDTO> getReceitasTributariaBruta() {
        return receitasTributariaBruta;
    }

    public void setReceitasTributariaBruta(List<ReceitaTributariaBrutaNfseDTO> receitasTributariaBruta) {
        this.receitasTributariaBruta = receitasTributariaBruta;
    }

    public List<ReceitaTributariaBrutaNfseDTO> getUltimasReceitasTributariaBruta() {
        return ultimasReceitasTributariaBruta;
    }

    public void setUltimasReceitasTributariaBruta(List<ReceitaTributariaBrutaNfseDTO> ultimasReceitasTributariaBruta) {
        this.ultimasReceitasTributariaBruta = ultimasReceitasTributariaBruta;
    }

    public Date getDataCredenciamentoISSOnline() {
        return dataCredenciamentoISSOnline;
    }

    public void setDataCredenciamentoISSOnline(Date dataCredenciamentoISSOnline) {
        this.dataCredenciamentoISSOnline = dataCredenciamentoISSOnline;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public Boolean getCriarUsuario() {
        return criarUsuario;
    }

    public void setCriarUsuario(Boolean criarUsuario) {
        this.criarUsuario = criarUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SituacaoCadastralNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoCadastralNfseDTO situacao) {
        this.situacao = situacao;
    }

    public BancoNfseDTO getBanco() {
        return banco;
    }

    public void setBanco(BancoNfseDTO banco) {
        this.banco = banco;
    }

    public Date getAbertura() {
        return abertura;
    }

    public void setAbertura(Date abertura) {
        this.abertura = abertura;
    }

    public Date getEncerramento() {
        return encerramento;
    }

    public void setEncerramento(Date encerramento) {
        this.encerramento = encerramento;
    }

    @Override
    public String toString() {
        return inscricaoMunicipal + " - " + pessoa.toString();
    }

    public void validarCampos() {
        NfseOperacaoNaoPermitidaException exe = new NfseOperacaoNaoPermitidaException();
        validarCampoInscricaoMunicipal(exe);
        validarCamposPessoais(exe);
        exe.lancarException();
        validarCamposSocios(exe);
        exe.lancarException();
        validarCamposEnquadramento(exe);
        exe.lancarException();
        validarCamposServicosCnaes(exe);
        exe.lancarException();
        exe.lancarException();
    }

    private void validarCampoInscricaoMunicipal(NfseOperacaoNaoPermitidaException exe) {
        if (Strings.isNullOrEmpty(this.inscricaoMunicipal)) {
            exe.adicionarMensagem("O Campo inscrição municipal é obrigatório.");
        }
    }

    private void validarCamposServicosCnaes(NfseOperacaoNaoPermitidaException exe) {
        if (this.servicos == null || this.servicos.isEmpty()) {
            exe.adicionarMensagem("É obrigatório ter pelo menos um serviço vinculado a empresa.");
        }
        if (this.cnaes == null || this.cnaes.isEmpty()) {
            exe.adicionarMensagem("É obrigatório ter pelo menos um cnae vinculado a empresa.");
        }
    }

    private void validarCamposEnquadramento(NfseOperacaoNaoPermitidaException exe) {
        if (this.enquadramentoFiscal == null) {
            exe.adicionarMensagem("O Enquadramento não foi encontrada.");
        } else {
            if (this.enquadramentoFiscal.getPorte() == null) {
                exe.adicionarMensagem("O Campo porte é obrigatório.");
            }
            if (this.enquadramentoFiscal.getTipoContribuinte() == null) {
                exe.adicionarMensagem("O Campo tipo de contribuinte é obrigatório.");
            }
            if (this.enquadramentoFiscal.getRegimeTributario() == null) {
                exe.adicionarMensagem("O Campo regime tributário é obrigatório.");
            }

            if (this.enquadramentoFiscal.getClassificacaoAtividade() == null) {
                exe.adicionarMensagem("O Campo Classificação da atividade é obrigatório.");
            }
            if (this.enquadramentoFiscal.getTipoNotaFiscal() == null) {
                exe.adicionarMensagem("O Campo tipo de nota fiscal é obrigatório.");
            }
        }
    }

    private void validarCamposSocios(NfseOperacaoNaoPermitidaException exe) {
        if (this.socios == null || this.socios.isEmpty()) {
            exe.adicionarMensagem("É obrigatório ter pelo menos um sócio vinculado a empresa.");
        } else {
            Double total = 0d;
            for (SocioNfseDTO socio : this.socios) {
                if (socio.getSocio() == null) {
                    exe.adicionarMensagem("O Sócio não foi encontrado.");
                } else {
                    if (Strings.isNullOrEmpty(socio.getSocio().getDadosPessoais().getCpfCnpj())) {
                        exe.adicionarMensagem("O campo cpf/cnpj do sócio é obrigatório.");
                    }
                    if (Strings.isNullOrEmpty(socio.getSocio().getDadosPessoais().getNomeRazaoSocial())) {
                        exe.adicionarMensagem("O campo nome do sócio é obrigatório.");
                    }
                    if (socio.getProporcao() == null) {
                        exe.adicionarMensagem("O campo proporção do sócio é obrigatório.");
                    } else {
                        if (socio.getProporcao() <= 0d) {
                            exe.adicionarMensagem("O campo proporção do sócio não pode ser igual ou menor que 0 (ZERO).");
                        } else {
                            total = total + socio.getProporcao();
                        }
                    }
                }
            }
            if (total != 100d) {
                exe.adicionarMensagem("A porcentagem total dos sócios é diferente de 100%.");
            }

        }
    }

    private void validarCamposPessoais(NfseOperacaoNaoPermitidaException exe) {
        if (this.pessoa == null) {
            exe.adicionarMensagem("A pessoa não foi encontrada.");
        } else {
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getCpfCnpj())) {
                exe.adicionarMensagem("O Campo cpf/cnpj é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getNomeRazaoSocial())) {
                exe.adicionarMensagem("O Campo nome/razão social é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getNomeFantasia())) {
                exe.adicionarMensagem("O Campo nome fantasia é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getInscricaoEstadualRg())) {
                exe.adicionarMensagem("O Campo RG/inscrição estadual é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getEmail())) {
                exe.adicionarMensagem("O Campo email é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getTelefone()) && Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getCelular())) {
                exe.adicionarMensagem("O Campo telefone ou celular é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getCep())) {
                exe.adicionarMensagem("O Campo cep é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getLogradouro())) {
                exe.adicionarMensagem("O Campo logradouro é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getNumero())) {
                exe.adicionarMensagem("O Campo número é obrigatório.");
            }
            if (Strings.isNullOrEmpty(this.pessoa.getDadosPessoais().getBairro())) {
                exe.adicionarMensagem("O Campo bairro é obrigatório.");
            }
            if (this.pessoa.getDadosPessoais().getMunicipio() == null) {
                exe.adicionarMensagem("O Campo município é obrigatório.");
            }
        }
    }

    @JsonIgnore
    public boolean hasServico(ServicoNfseDTO servico) {
        if (servico == null) {
            return false;
        }
        for (ServicoNfseDTO servicoCadastro : this.servicos) {
            if (servicoCadastro.getId().equals(servico.getId())) {
                return true;
            }
        }
        return false;
    }
}
