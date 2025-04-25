package br.com.webpublico.domain.dto.util;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.RegimeTributarioNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.nfse10.*;
import com.google.common.base.Strings;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

public class NfseXMLV1 {

    public static TcCompNfse criarTcCompNfse(NotaFiscalNfseDTO notaFiscalNfseDTO, ConfiguracaoNfseDTO configuracao) throws DatatypeConfigurationException {
        TcCompNfse tcCompNfse = new TcCompNfse();
        tcCompNfse.setNfse(new TcNfse());
        tcCompNfse.getNfse().setInfNfse(new TcInfNfse());
        tcCompNfse.getNfse().getInfNfse().setSerie("");
        tcCompNfse.getNfse().getInfNfse().setTipo("");
        tcCompNfse.getNfse().getInfNfse().setNaturezaOperacao(notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getNaturezaOperacao().getCodigoV1().byteValue());
        tcCompNfse.getNfse().getInfNfse().setIdentificacaoRps(new TcIdentificacaoRps());
        if (RegimeTributarioNfseDTO.SIMPLES_NACIONAL.equals(notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().getRegimeTributario())) {
            tcCompNfse.getNfse().getInfNfse().setOptanteSimplesNacional(new Integer(1).byteValue());
        }
        tcCompNfse.getNfse().getInfNfse().setOptanteSimplesNacional(new Integer(1).byteValue());
        if (notaFiscalNfseDTO.getRps() != null && notaFiscalNfseDTO.getRps().getNumero() != null) {
            tcCompNfse.getNfse().getInfNfse().getIdentificacaoRps().setNumero(new BigInteger(notaFiscalNfseDTO.getRps().getNumero()));
            tcCompNfse.getNfse().getInfNfse().getIdentificacaoRps().setSerie(notaFiscalNfseDTO.getRps().getSerie());
            if (notaFiscalNfseDTO.getRps().getTipoRps() != null)
                tcCompNfse.getNfse().getInfNfse().getIdentificacaoRps().setTipo(new Integer(notaFiscalNfseDTO.getRps().getTipoRps().getCodigo()).byteValue());
            GregorianCalendar emissaoRps = new GregorianCalendar();
            emissaoRps.setTime(notaFiscalNfseDTO.getRps().getDataEmissao());
            tcCompNfse.getNfse().getInfNfse().setDataEmissaoRps(DatatypeFactory.newInstance().newXMLGregorianCalendar(emissaoRps));
        } else {
            tcCompNfse.getNfse().getInfNfse().getIdentificacaoRps().setNumero(BigInteger.ZERO);
            tcCompNfse.getNfse().getInfNfse().getIdentificacaoRps().setSerie("");
            tcCompNfse.getNfse().getInfNfse().getIdentificacaoRps().setTipo(new Integer(0).byteValue());
            tcCompNfse.getNfse().getInfNfse().setDataEmissaoRps(null);
        }
        tcCompNfse.getNfse().getInfNfse().setOrgaoGerador(new TcIdentificacaoOrgaoGerador());
        tcCompNfse.getNfse().getInfNfse().setCodigoVerificacao(notaFiscalNfseDTO.getCodigoVerificacao());
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(notaFiscalNfseDTO.getEmissao());
        tcCompNfse.getNfse().getInfNfse().setDataEmissao(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        tcCompNfse.getNfse().getInfNfse().setNumero(new BigInteger(notaFiscalNfseDTO.getNumero().toString()));
        tcCompNfse.getNfse().getInfNfse().setNfseSubstituida(null);
        tcCompNfse.getNfse().getInfNfse().setOutrasInformacoes(notaFiscalNfseDTO.getDescriminacaoServico());
        tcCompNfse.getNfse().getInfNfse().setValorCredito(BigDecimal.ZERO);
        tcCompNfse.getNfse().getInfNfse().setLinkAutenticacao(configuracao.getUrlAutenticao() + notaFiscalNfseDTO.getId());

        tcCompNfse.getNfse().getInfNfse().setServico(criarTcDadosServico(notaFiscalNfseDTO));
        if (!notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getItens().isEmpty()) {
            ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getItens().get(0);
            tcCompNfse.getNfse().getInfNfse().getServico().getValores().setAliquota(itemDeclaracaoServico.getAliquotaServico());
            if (itemDeclaracaoServico.getMunicipio() != null && !Strings.isNullOrEmpty(itemDeclaracaoServico.getMunicipio().getCodigoIbge())) {
                tcCompNfse.getNfse().getInfNfse().getOrgaoGerador().setCodigoMunicipio(new Integer(itemDeclaracaoServico.getMunicipio().getCodigoIbge()));
                tcCompNfse.getNfse().getInfNfse().getOrgaoGerador().setUf(itemDeclaracaoServico.getMunicipio().getEstado());
            }
        }

        GregorianCalendar competencia = new GregorianCalendar();
        competencia.setTime(notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getCompetencia());
        tcCompNfse.getNfse().getInfNfse().setCompetencia(DatatypeFactory.newInstance().newXMLGregorianCalendar(competencia));

        tcCompNfse.getNfse().getInfNfse().setPrestadorServico(criarTcDadosPrestador(notaFiscalNfseDTO.getPrestador()));
        tcCompNfse.getNfse().getInfNfse().setTomadorServico(criarTcDadosTomador(notaFiscalNfseDTO.getTomador()));
        tcCompNfse.getNfse().getInfNfse().setContrucaoCivil(criarTcConstrucaoCivil(notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getConstrucaoCivil()));

        if (SituacaoDeclaracaoNfseDTO.CANCELADA.equals(notaFiscalNfseDTO.getSituacao())) {
            tcCompNfse.setNfseCancelamento(criarTcCancelamentoNfse(notaFiscalNfseDTO));
        }
        return tcCompNfse;
    }

    private static TcCancelamentoNfse criarTcCancelamentoNfse(NotaFiscalNfseDTO notaFiscalNfseDTO) throws DatatypeConfigurationException {
        CancelamentoNfseDTO cancelamentoNfseDTO = notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getCancelamento();
        if (cancelamentoNfseDTO == null) {
            return new TcCancelamentoNfse();
        }
        TcCancelamentoNfse tcCancelamentoNfse = new TcCancelamentoNfse();
        tcCancelamentoNfse.setConfirmacao(new TcConfirmacaoCancelamento());
        GregorianCalendar competencia = new GregorianCalendar();
        competencia.setTime(cancelamentoNfseDTO.getDataCancelamento());
        tcCancelamentoNfse.getConfirmacao().setDataHoraCancelamento(DatatypeFactory.newInstance().newXMLGregorianCalendar(competencia));
        tcCancelamentoNfse.getConfirmacao().setPedido(new TcPedidoCancelamento());
        tcCancelamentoNfse.getConfirmacao().getPedido().setInfPedidoCancelamento(new TcInfPedidoCancelamento());
        tcCancelamentoNfse.getConfirmacao().getPedido().getInfPedidoCancelamento().setCodigoCancelamento("1");
        return tcCancelamentoNfse;
    }

    private static TcDadosConstrucaoCivil criarTcConstrucaoCivil(ConstrucaoCivilNfseDTO construcaoCivil) {
        TcDadosConstrucaoCivil tcDadosConstrucaoCivil = null;
        if (construcaoCivil != null) {
            tcDadosConstrucaoCivil = new TcDadosConstrucaoCivil();
            tcDadosConstrucaoCivil.setArt(construcaoCivil.getArt());
            tcDadosConstrucaoCivil.setCodigoObra(construcaoCivil.getCodigoObra().toString());
        }
        return tcDadosConstrucaoCivil;
    }

    private static TcDadosServico criarTcDadosServico(NotaFiscalNfseDTO notaFiscalNfseDTO) {
        DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico = notaFiscalNfseDTO.getDeclaracaoPrestacaoServico();
        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = notaFiscalNfseDTO.getDeclaracaoPrestacaoServico().getItens().get(0);

        TcDadosServico tcDadosServico = new TcDadosServico();

        if (itemDeclaracaoServico.getMunicipio() != null && !Strings.isNullOrEmpty(itemDeclaracaoServico.getMunicipio().getCodigoIbge())) {
            tcDadosServico.setCodigoMunicipio(new Integer(itemDeclaracaoServico.getMunicipio().getCodigoIbge()));
        }


        tcDadosServico.setDiscriminacao(notaFiscalNfseDTO.getDescriminacaoServico());
//        tcDadosServico.setMunicipioIncidencia();

        tcDadosServico.setValores(criarTcValoresDeclaracaoServico(notaFiscalNfseDTO));

        tcDadosServico.setItemListaServico(declaracaoPrestacaoServico.getItens().get(0).getServico().getCodigo());
        return tcDadosServico;
    }

    private static TcValores criarTcValoresDeclaracaoServico(NotaFiscalNfseDTO notaFiscal) {
        DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico = notaFiscal.getDeclaracaoPrestacaoServico();
        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = declaracaoPrestacaoServico.getItens().get(0);
        TributosFederaisNfseDTO tributosFederais = declaracaoPrestacaoServico.getTributosFederais();

        TcValores tcValoresDeclaracaoServico = new TcValores();
        tcValoresDeclaracaoServico.setAliquota(itemDeclaracaoServico.getAliquotaServico());
        tcValoresDeclaracaoServico.setDescontoCondicionado(itemDeclaracaoServico.getDescontosCondicionados());
        tcValoresDeclaracaoServico.setDescontoIncondicionado(itemDeclaracaoServico.getDescontosIncondicionados());
        tcValoresDeclaracaoServico.setOutrasRetencoes(tributosFederais.getOutrasRetencoes());
        tcValoresDeclaracaoServico.setValorCofins(tributosFederais.getCofins());
        tcValoresDeclaracaoServico.setValorCsll(tributosFederais.getCsll());
        tcValoresDeclaracaoServico.setValorInss(tributosFederais.getInss());
        tcValoresDeclaracaoServico.setValorIr(tributosFederais.getIrrf());
        tcValoresDeclaracaoServico.setValorPis(tributosFederais.getPis());
        tcValoresDeclaracaoServico.setValorDeducoes(notaFiscal.getDeducoesLegais());
        tcValoresDeclaracaoServico.setValorIss(notaFiscal.getIssPagar());
        tcValoresDeclaracaoServico.setValorServicos(notaFiscal.getTotalServicos());
        tcValoresDeclaracaoServico.setValorLiquidoNfse(notaFiscal.getValorLiquido());
        tcValoresDeclaracaoServico.setBaseCalculo(notaFiscal.getBaseCalculo());
        if (notaFiscal.getDeclaracaoPrestacaoServico().getIssRetido()) {
            tcValoresDeclaracaoServico.setIssRetido(new Integer(1).byteValue());
            tcValoresDeclaracaoServico.setValorIss(BigDecimal.ZERO);
            tcValoresDeclaracaoServico.setValorIssRetido(notaFiscal.getIssCalculado());
        } else {
            tcValoresDeclaracaoServico.setIssRetido(new Integer(2).byteValue());
            tcValoresDeclaracaoServico.setValorIss(notaFiscal.getIssCalculado());
            tcValoresDeclaracaoServico.setValorIssRetido(BigDecimal.ZERO);
        }
        return tcValoresDeclaracaoServico;
    }

    private static TcDadosTomador criarTcDadosTomador(TomadorServicoDTO tomador) {
        if (tomador == null || tomador.getDadosPessoais() == null) {
            return null;
        }
        TcDadosTomador tcDadosTomador = new TcDadosTomador();
        tcDadosTomador.setRazaoSocial(tomador.getDadosPessoais().getNomeRazaoSocial());

        tcDadosTomador.setIdentificacaoTomador(new TcIdentificacaoTomador());
        tcDadosTomador.getIdentificacaoTomador().setCpfCnpj(new TcCpfCnpj());
        if (tomador.getDadosPessoais().isJuridica()) {
            tcDadosTomador.getIdentificacaoTomador().getCpfCnpj().setCnpj(tomador.getDadosPessoais().getCpfCnpj());
        } else {
            tcDadosTomador.getIdentificacaoTomador().getCpfCnpj().setCpf(tomador.getDadosPessoais().getCpfCnpj());
        }

        tcDadosTomador.setContato(new TcContato());
        tcDadosTomador.getContato().setEmail(tomador.getDadosPessoais().getEmail());
        tcDadosTomador.getContato().setTelefone(tomador.getDadosPessoais().getTelefone());

        tcDadosTomador.setEndereco(new TcEndereco());
        tcDadosTomador.getEndereco().setBairro(tomador.getDadosPessoais().getBairro());
        tcDadosTomador.getEndereco().setEndereco(tomador.getDadosPessoais().getLogradouro());
        if (!Strings.isNullOrEmpty(tomador.getDadosPessoais().getCep())) {
            String cep = StringUtils.getApenasNumeros(tomador.getDadosPessoais().getCep());
            if (!Strings.isNullOrEmpty(cep))
                try {
                    tcDadosTomador.getEndereco().setCep(Integer.valueOf(cep));
                } catch (Exception e) {
                }
        }
        if (tomador.getDadosPessoais().getMunicipio() != null && !Strings.isNullOrEmpty(tomador.getDadosPessoais().getMunicipio().getCodigoIbge())) {
            tcDadosTomador.getEndereco().setCodigoMunicipio(new Integer(tomador.getDadosPessoais().getMunicipio().getCodigoIbge()));
            tcDadosTomador.getEndereco().setUf(tomador.getDadosPessoais().getMunicipio().getEstado());
        }
        if (tomador.getDadosPessoais().getPais() != null && tomador.getDadosPessoais().getPais().getCodigo() != null) {
            tcDadosTomador.getEndereco().setCodigoPais(tomador.getDadosPessoais().getPais().getCodigo());
        }
        tcDadosTomador.getEndereco().setComplemento(tomador.getDadosPessoais().getComplemento());
        tcDadosTomador.getEndereco().setNumero(tomador.getDadosPessoais().getNumero());
        return tcDadosTomador;

    }

    private static TcDadosPrestador criarTcDadosPrestador(PrestadorServicoNfseDTO prestador) {
        TcDadosPrestador tcDadosPrestador = new TcDadosPrestador();
        tcDadosPrestador.setRazaoSocial(prestador.getPessoa().getDadosPessoais().getNomeRazaoSocial());
        tcDadosPrestador.setNomeFantasia(prestador.getPessoa().getDadosPessoais().getNomeFantasia());

        tcDadosPrestador.setIdentificacaoPrestador(new TcIdentificacaoPrestador());
        tcDadosPrestador.getIdentificacaoPrestador().setCnpj(prestador.getPessoa().getDadosPessoais().getCpfCnpj());
        tcDadosPrestador.getIdentificacaoPrestador().setInscricaoMunicipal(prestador.getInscricaoMunicipal());

        tcDadosPrestador.setContato(new TcContato());
        tcDadosPrestador.getContato().setEmail(prestador.getPessoa().getDadosPessoais().getEmail());
        tcDadosPrestador.getContato().setTelefone(prestador.getPessoa().getDadosPessoais().getTelefone());

        tcDadosPrestador.setEndereco(new TcEndereco());
        tcDadosPrestador.getEndereco().setBairro(prestador.getPessoa().getDadosPessoais().getBairro());
        tcDadosPrestador.getEndereco().setEndereco(prestador.getPessoa().getDadosPessoais().getLogradouro());
        if (!Strings.isNullOrEmpty(prestador.getPessoa().getDadosPessoais().getCep()))
            tcDadosPrestador.getEndereco().setCep(Integer.valueOf(prestador.getPessoa().getDadosPessoais().getCep().replaceAll("\\D+", "")));
        if (prestador.getPessoa().getDadosPessoais().getMunicipio() != null && !Strings.isNullOrEmpty(prestador.getPessoa().getDadosPessoais().getMunicipio().getCodigoIbge())) {
            tcDadosPrestador.getEndereco().setCodigoMunicipio(new Integer(prestador.getPessoa().getDadosPessoais().getMunicipio().getCodigoIbge()));
            tcDadosPrestador.getEndereco().setUf(prestador.getPessoa().getDadosPessoais().getMunicipio().getEstado());
        }
        tcDadosPrestador.getEndereco().setComplemento(prestador.getPessoa().getDadosPessoais().getComplemento());
        tcDadosPrestador.getEndereco().setNumero(prestador.getPessoa().getDadosPessoais().getNumero());
        return tcDadosPrestador;
    }

}
