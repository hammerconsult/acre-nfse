package br.com.webpublico.domain.dto.template;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.TributosFederaisNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.template.Field;
import br.com.webpublico.domain.dto.template.TagNotaFiscal;
import br.com.webpublico.domain.dto.template.TrocaTag;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by rodolfo on 14/02/18.
 */
public class TrocaTagNotaFiscal extends TrocaTag {
    private final NotaFiscalNfseDTO notaFiscal;

    public TrocaTagNotaFiscal(NotaFiscalNfseDTO notaFiscal, ConfiguracaoNfseDTO configuracaoTributario) {
        super(TipoTemplateNfse.NFSE, configuracaoTributario);
        this.notaFiscal = notaFiscal;
        addicionarFields();
    }

    public void addicionarFields() {
        addTagsHeader();
        addFieldsPrestador(notaFiscal.getPrestador());
        addTomador();
//        addItensServico();
        addTagsRentencoesFederais();
        addTagsValores();
    }

//    private void addItensServico() {
//        TemplateNfseDTO templateItem = templateService.buscarPorTipo(TipoTemplateNfse.ITEM_SERVICO);
//        String itens = new TrocaTagItemServico(notaFiscal, configuracaoNfse).trocarTags(templateItem.getConteudo());
//        addicionarField(new Field(TagNotaFiscal.TABELA_ITENS, itens));
//    }

    private void addTomador() {
        DadosPessoaisNfseDTO tomador = notaFiscal.getTomador().getDadosPessoais();
        if (tomador != null) {
            addicionarField(new Field(TagNotaFiscal.NOME_FANTASIA_TOMADOR, tomador.getNomeFantasia()));
            addicionarField(new Field(TagNotaFiscal.RAZAO_SOCIAL_TOMADOR, tomador.getNomeRazaoSocial()));
            addicionarField(new Field(TagNotaFiscal.CPF_CNPJ_TOMADOR, tomador.getCpfCnpj()));
            addicionarField(new Field(TagNotaFiscal.EMAIL_TOMADOR, tomador.getEmail()));
            addicionarField(new Field(TagNotaFiscal.LOGRADOURO_TOMADOR, tomador.getLogradouro()));
            addicionarField(new Field(TagNotaFiscal.BAIRRO_TOMADOR, tomador.getBairro()));
            addicionarField(new Field(TagNotaFiscal.CEP_TOMADOR, tomador.getCep()));
            addicionarField(new Field(TagNotaFiscal.COMPLEMENTO_TOMADOR, tomador.getComplemento()));
            if (tomador.getMunicipio() != null) {
                addicionarField(new Field(TagNotaFiscal.MUNICIPIO_TOMADOR, tomador.getMunicipio().getNome()));
            }

        }
    }

    private void addTagsHeader() {
        addicionarField(new Field(TagNotaFiscal.DATA_FATO, dateFormat.print(DateTime.now())));
        addicionarField(new Field(TagNotaFiscal.EMISSAO, dateFormat.print(notaFiscal.getEmissao().getTime())));
        addicionarField(new Field(TagNotaFiscal.NUMERO_NOTA, notaFiscal.getNumero().toString()));
        addicionarField(new Field(TagNotaFiscal.CODIGO_AUTENTICIDADE, notaFiscal.getCodigoVerificacao()));
        addicionarField(new Field(TagNotaFiscal.NUMERO_RPS, "0"));
        addicionarField(new Field(TagNotaFiscal.LOGO_EMPRESA, " "));
//        if (notaFiscal.getPrestador().getPessoa().getArquivo() != null) {
//            addicionarField(new Field(TagNotaFiscal.LOGO_EMPRESA,
//                "<img src='" + FacesUtil.getBaseUrl(request) + "/nfse/arquivo/" + notaFiscal.getPrestador().getPessoa().getArquivo().getId() + "' style='width: 100%'/>"));
//        }
    }


    private void addTagsRentencoesFederais() {
        TributosFederaisNfseDTO tributosFederais = notaFiscal.getDeclaracaoPrestacaoServico().getTributosFederais();
        if (tributosFederais != null) {
            addicionarField(new Field(TagNotaFiscal.PIS, decimalFormat.format(tributosFederais.getPis())));
            addicionarField(new Field(TagNotaFiscal.COFINS, decimalFormat.format(tributosFederais.getCofins())));
            addicionarField(new Field(TagNotaFiscal.INSS, decimalFormat.format(tributosFederais.getInss())));
            addicionarField(new Field(TagNotaFiscal.IR, decimalFormat.format(tributosFederais.getIrrf())));
            addicionarField(new Field(TagNotaFiscal.CSLL, decimalFormat.format(tributosFederais.getCsll())));
            addicionarField(new Field(TagNotaFiscal.OUTRAS_RETENCOES, decimalFormat.format(tributosFederais.getOutrasRetencoes())));
        } else {
            addicionarField(new Field(TagNotaFiscal.PIS, decimalFormat.format(BigDecimal.ZERO)));
            addicionarField(new Field(TagNotaFiscal.COFINS, decimalFormat.format(BigDecimal.ZERO)));
            addicionarField(new Field(TagNotaFiscal.INSS, decimalFormat.format(BigDecimal.ZERO)));
            addicionarField(new Field(TagNotaFiscal.IR, decimalFormat.format(BigDecimal.ZERO)));
            addicionarField(new Field(TagNotaFiscal.CSLL, decimalFormat.format(BigDecimal.ZERO)));
            addicionarField(new Field(TagNotaFiscal.OUTRAS_RETENCOES, decimalFormat.format(BigDecimal.ZERO)));
        }
    }

    private void addTagsValores() {
        addicionarField(new Field(TagNotaFiscal.TOTAL, decimalFormat.format(notaFiscal.getTotalServicos())));
        addicionarField(new Field(TagNotaFiscal.VALOR_LIQUIDO, decimalFormat.format(notaFiscal.getTotalServicos())));
        addicionarField(new Field(TagNotaFiscal.DESCONTO_CONDICIONADO, decimalFormat.format(BigDecimal.ZERO)));
        addicionarField(new Field(TagNotaFiscal.DESCONTO_INCONDICIONADO, decimalFormat.format(BigDecimal.ZERO)));
        addicionarField(new Field(TagNotaFiscal.DEDUCOES, decimalFormat.format(BigDecimal.ZERO)));
        addicionarField(new Field(TagNotaFiscal.BASE_CALCULO, decimalFormat.format(notaFiscal.getBaseCalculo())));
        addicionarField(new Field(TagNotaFiscal.VALOR_ISS, decimalFormat.format(notaFiscal.getIssCalculado() != null ? notaFiscal.getIssCalculado() : BigDecimal.ZERO)));

    }
}
