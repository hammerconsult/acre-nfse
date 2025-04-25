package br.com.webpublico.domain.dto.template;


import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import com.google.common.collect.Lists;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

public abstract class TrocaTag<T extends TipoTemplateNfse> {
    protected final static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");
    protected final static DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    protected final ConfiguracaoNfseDTO configuracaoNfse;
    private List<Field> fields = Lists.newArrayList();
    private T tipoTemplate;

    TrocaTag(T tipoTemplate, ConfiguracaoNfseDTO configuracaoNfse) {
        this.tipoTemplate = tipoTemplate;
        this.configuracaoNfse = configuracaoNfse;
    }

    public List<Field> getFields() {
        return fields;
    }

    void addicionarField(Field field) {
        fields.add(field);
    }

    public T getTipoTemplate() {
        return tipoTemplate;
    }

    public String trocarTags(String conteudo) {
        try {
            Properties p = new Properties();
            p.setProperty("resource.loader", "string");
            p.setProperty("string.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            VelocityEngine ve = new VelocityEngine();
            ve.init(p);

            VelocityContext context = new VelocityContext();
            for (Field field : fields) {
                if (field.getValue() != null) {
                    context.put(field.getTag().name(), field.getValue());
                } else {
                    context.put(field.getTag().name(), "");
                }
            }
            StringWriter writer = new StringWriter();
            ve.evaluate(context, writer, "str", conteudo);
            return writer.toString();
        } catch (Exception e) {
            return conteudo;
        }
    }

    public void addFieldsUser(UserNfseDTO user) {
        addicionarField(new Field(TagUsuario.CPF_CNPJ_USUARIO, user.getLogin()));
        addicionarField(new Field(TagUsuario.NOME_RAZAOSOCIAL_USUARIO, user.getNome()));
    }

    public void addFieldsPrestador(PrestadorServicoNfseDTO cadastroEconomico) {
        addFieldsPrestador(cadastroEconomico.getPessoa().getDadosPessoais());
    }

    public void addFieldsPrestador(DadosPessoaisNfseDTO prestador) {
        if (prestador != null) {
            addicionarField(new Field(TagPrestadorServicos.NOME_FANTASIA_PRESTADOR, prestador.getNomeFantasia()));
            addicionarField(new Field(TagPrestadorServicos.RAZAO_SOCIAL_PRESTADOR, prestador.getNomeRazaoSocial()));
            addicionarField(new Field(TagPrestadorServicos.CPF_CNPJ_PRESTADOR, prestador.getCpfCnpj()));
            addicionarField(new Field(TagPrestadorServicos.EMAIL_PRESTADOR, prestador.getEmail()));
            addicionarField(new Field(TagPrestadorServicos.LOGRADOURO_PRESTADOR, prestador.getLogradouro()));
            addicionarField(new Field(TagPrestadorServicos.BAIRRO_PRESTADOR, prestador.getBairro()));
            addicionarField(new Field(TagPrestadorServicos.CEP_PRESTADOR, prestador.getCep()));
            addicionarField(new Field(TagPrestadorServicos.COMPLEMENTO_PRESTADOR, prestador.getComplemento()));
            if (prestador.getMunicipio() != null) {
                addicionarField(new Field(TagPrestadorServicos.MUNICIPIO_PRESTADOR, prestador.getMunicipio().getNome()));
            }
        }
    }

}
