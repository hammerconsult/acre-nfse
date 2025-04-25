package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.domain.dto.TomadorServicoDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.template.Field;
import br.com.webpublico.domain.dto.template.TagEmailNotaFiscal;
import br.com.webpublico.domain.dto.template.TrocaTag;
import br.com.webpublico.domain.dto.util.Util;

/**
 * Created by rodolfo on 14/02/18.
 */
public class TrocaTagEmailNotaFiscal extends TrocaTag {

    private NotaFiscalNfseDTO notaFiscal;

    public TrocaTagEmailNotaFiscal(NotaFiscalNfseDTO notaFiscal, ConfiguracaoNfseDTO configuracaoTributario) {
        super(TipoTemplateNfse.EMAIL_NOTAFISCAL, configuracaoTributario);
        this.notaFiscal = notaFiscal;
        addicionarFields();
    }

    public void addicionarFields() {
        addicionarField(new Field(TagEmailNotaFiscal.NUMERO, notaFiscal.getNumero().toString()));
        addicionarField(new Field(TagEmailNotaFiscal.EMISSAO, Util.formatterDiaMesAno.format(notaFiscal.getEmissao())));
        addicionarField(new Field(TagEmailNotaFiscal.VALOR, Util.formataValor(notaFiscal.getValorLiquido())));
        addicionarField(new Field(TagEmailNotaFiscal.CODIGO, notaFiscal.getCodigoVerificacao()));
        addicionarField(new Field(TagEmailNotaFiscal.LINK_NOTA, configuracaoNfse.getUrlNfse() + "/impressao/" + notaFiscal.getId()));
        addicionarField(new Field(TagEmailNotaFiscal.LINK_SISTEMA, configuracaoNfse.getUrlNfse()));
        PessoaNfseDTO prestador = notaFiscal.getPrestador().getPessoa();
        if (prestador != null) {
            addFieldsPrestador(prestador.getDadosPessoais());
        }
        TomadorServicoDTO tomador = notaFiscal.getTomador();
        if (tomador != null && tomador.getDadosPessoais() != null) {
            addicionarField(new Field(TagEmailNotaFiscal.CPF_CNPJ_TOMADOR, tomador.getDadosPessoais().getCpfCnpj()));
        }
    }
}
