package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.template.Field;
import br.com.webpublico.domain.dto.template.TagEmailAtivacaoUsuario;
import br.com.webpublico.domain.dto.template.TrocaTag;

public class TrocaTagEmailAtivacaoUsuario extends TrocaTag {
    private UserNfseDTO user;
    private MunicipioNfseDTO municipio;

    public TrocaTagEmailAtivacaoUsuario(UserNfseDTO user, ConfiguracaoNfseDTO configuracaoNfse) {
        super(TipoTemplateNfse.ATIVACAO_CADASTRO, configuracaoNfse);
        this.user = user;
        this.municipio = configuracaoNfse.getMunicipio();
        adicionarFields();
    }

    public void adicionarFields() {
        addicionarField(new Field(TagEmailAtivacaoUsuario.MUNICIPIO, municipio.getNome()));
        addicionarField(new Field(TagEmailAtivacaoUsuario.USUARIO, user.getNome()));
        addicionarField(new Field(TagEmailAtivacaoUsuario.LINK, configuracaoNfse.getUrlNfse() + "/#/activate?key=" + user.getActivationKey()));
    }
}
