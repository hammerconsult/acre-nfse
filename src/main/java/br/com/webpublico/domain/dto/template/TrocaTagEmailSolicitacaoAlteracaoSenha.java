package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.template.Field;
import br.com.webpublico.domain.dto.template.TagEmailSolicitacaoAlteracaoSenha;
import br.com.webpublico.domain.dto.template.TagEmailSolicitante;
import br.com.webpublico.domain.dto.template.TrocaTag;

public class TrocaTagEmailSolicitacaoAlteracaoSenha extends TrocaTag {
    private UserNfseDTO user;
    private MunicipioNfseDTO municipio;

    public TrocaTagEmailSolicitacaoAlteracaoSenha(UserNfseDTO user, ConfiguracaoNfseDTO configuracaoTributario) {
        super(TipoTemplateNfse.SOLICITACAO_ALTERACAO_SENHA, configuracaoTributario);
        this.user = user;
        this.municipio = configuracaoTributario.getMunicipio();
        adicionarFields();
    }

    public void adicionarFields() {
        addicionarField(new Field(TagEmailSolicitacaoAlteracaoSenha.MUNICIPIO, municipio.getNome()));
        addicionarField(new Field(TagEmailSolicitacaoAlteracaoSenha.USUARIO, user.getNome()));
        addicionarField(new Field(TagEmailSolicitacaoAlteracaoSenha.CPFCNPJ, user.getLogin()));
        addicionarField(new Field(TagEmailSolicitante.LINK, configuracaoNfse.getUrlNfse() +
                "/#/reset/finish?key=" + user.getResetKey()));
    }
}
