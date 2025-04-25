package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import com.google.common.collect.Lists;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

/**
 * Created by rodolfo on 14/02/18.
 */
public class TrocaTagItemServico extends TrocaTag {
    private NotaFiscalNfseDTO notaFiscal;

    public TrocaTagItemServico(NotaFiscalNfseDTO notaFiscal, ConfiguracaoNfseDTO configuracaoTributario) {
        super(TipoTemplateNfse.ITEM_SERVICO, configuracaoTributario);
        this.notaFiscal = notaFiscal;
    }

    @Override
    public String trocarTags(String conteudo) {
        try {
            Properties p = new Properties();

            p.setProperty("resource.loader", "string");
            p.setProperty("string.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            VelocityEngine ve = new VelocityEngine();
            ve.init(p);


            VelocityContext context = new VelocityContext();
            StringWriter writer = new StringWriter();
            for (ItemDeclaracaoServicoNfseDTO item : notaFiscal.getDeclaracaoPrestacaoServico().getItens()) {
                for (Field field : getFieldsLocais(item)) {
                    if (field.getValue() != null) {
                        context.put(field.getTag().name(), field.getValue());
                    }
                }
                ve.evaluate(context, writer, "str", conteudo);
            }
            return writer.toString();
        } catch (Exception e) {
            return conteudo;
        }
    }

    private List<Field> getFieldsLocais(ItemDeclaracaoServicoNfseDTO item) {
        List<Field> fields = Lists.newArrayList();
        fields.add(new Field(TagItemServico.NOME, item.getNomeServico()));
        fields.add(new Field(TagItemServico.VALOR, decimalFormat.format(item.getValorServico())));
        fields.add(new Field(TagItemServico.QUANTIDADE, item.getQuantidade().toString()));
        fields.add(new Field(TagItemServico.TOTAL_SERVICO, decimalFormat.format(item.getValorServico())));
        fields.add(new Field(TagItemServico.BASE_CALCULO, decimalFormat.format(item.getBaseCalculo())));
        fields.add(new Field(TagItemServico.ISS, decimalFormat.format(item.getIss() != null ? item.getIss() : BigDecimal.ZERO)));

        return fields;
    }
}
