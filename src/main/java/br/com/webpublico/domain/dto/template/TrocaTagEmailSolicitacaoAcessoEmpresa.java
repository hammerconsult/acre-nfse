package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.template.Field;
import br.com.webpublico.domain.dto.template.TagComum;
import br.com.webpublico.domain.dto.template.TrocaTag;

public class TrocaTagEmailSolicitacaoAcessoEmpresa extends TrocaTag {

    private PrestadorServicoNfseDTO prestadorServicos;
    private MunicipioNfseDTO municipio;
    private UserNfseDTO user;

    public TrocaTagEmailSolicitacaoAcessoEmpresa(PrestadorServicoNfseDTO prestadorServicos,
                                                 UserNfseDTO user, ConfiguracaoNfseDTO configuracaoTributario) {
        super(TipoTemplateNfse.SOLICITACAO_ACESSO_EMPRESA, configuracaoTributario);
        this.prestadorServicos = prestadorServicos;
        this.user = user;
        this.municipio = configuracaoTributario.getMunicipio();
        adicionarFields();
    }

    public void adicionarFields() {
        addFieldsUser(user);
        addFieldsPrestador(prestadorServicos);
        addicionarField(new Field(TagComum.MUNICIPIO, municipio.getNome()));
        addicionarField(new Field(TagComum.ESTADO, municipio.getEstado()));

    }
}
