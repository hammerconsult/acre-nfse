package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.template.Field;
import br.com.webpublico.domain.dto.template.TagComum;
import br.com.webpublico.domain.dto.template.TagUsuario;
import br.com.webpublico.domain.dto.template.TrocaTag;

public class TrocaTagEmailSolicitacaoAcessoSemCadastro extends TrocaTag {

    private PrestadorServicoNfseDTO prestadorServicos;
    private MunicipioNfseDTO municipio;
    private DadosPessoaisNfseDTO user;

    public TrocaTagEmailSolicitacaoAcessoSemCadastro(PrestadorServicoNfseDTO prestadorServicos,
                                                     DadosPessoaisNfseDTO user, ConfiguracaoNfseDTO configuracaoTributario) {
        super(TipoTemplateNfse.EMAIL_SOLICITADO_ACESSO_SEM_CADASTRO, configuracaoTributario);
        this.prestadorServicos = prestadorServicos;
        this.user = user;
        this.municipio = configuracaoTributario.getMunicipio();
        addicionarFields();
    }

    public void addicionarFields() {
        addFieldsPrestador(prestadorServicos);
        addicionarField(new Field(TagComum.MUNICIPIO, municipio.getNome()));
        addicionarField(new Field(TagComum.ESTADO, municipio.getEstado()));
        addicionarField(new Field(TagUsuario.CPF_CNPJ_USUARIO, user.getCpfCnpj()));
        addicionarField(new Field(TagUsuario.NOME_RAZAOSOCIAL_USUARIO, user.getNomeRazaoSocial()));
        addicionarField(new Field(TagEmailSolicitante.LINK, configuracaoNfse.getUrlNfse()));
    }
}
