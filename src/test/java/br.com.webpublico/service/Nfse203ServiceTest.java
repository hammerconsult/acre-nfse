package br.com.webpublico.service;

import br.com.webpublico.ApplicationContextTest;

public class Nfse203ServiceTest extends ApplicationContextTest {

//    @Autowired
//    private Nfse203Service nfse203Service;
//
////    @Test
//    public void cancelarNfse() throws DatatypeConfigurationException, ValidacaoNfseWSException {
//        CancelarNfseEnvio cancelarNfseEnvio = new CancelarNfseEnvio();
//
//        cancelarNfseEnvio.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        cancelarNfseEnvio.getIdentificacaoRequerente().setInscricaoMunicipal("9033572");
//        cancelarNfseEnvio.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        cancelarNfseEnvio.getIdentificacaoRequerente().getCpfCnpj().setCnpj("09032577000150");
//
//        cancelarNfseEnvio.setPedido(new TcPedidoCancelamento());
//        cancelarNfseEnvio.getPedido().setInfPedidoCancelamento(new TcInfPedidoCancelamento());
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().setCodigoCancelamento(new Long(1).byteValue());
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().setIdentificacaoNfse(new TcIdentificacaoNfse());
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setNumero(new BigInteger("3"));
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setCpfCnpj(new TcCpfCnpj());
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setInscricaoMunicipal("9033572");
//
//        CancelarNfseResposta cancelarNfseResposta = nfse203Service.cancelar(cancelarNfseEnvio);
//
//        Assert.assertNotNull(cancelarNfseResposta);
//        Assert.assertNotNull(cancelarNfseResposta.getTcRetCancelamento());
//    }
//
//    @Test
//    public void consultarNfseFaixa() throws Exception {
//        ConsultarNfseFaixaEnvio consultarNfseFaixaEnvio = new ConsultarNfseFaixaEnvio();
//        consultarNfseFaixaEnvio.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        consultarNfseFaixaEnvio.getIdentificacaoRequerente().setInscricaoMunicipal("9033572");
//        consultarNfseFaixaEnvio.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        consultarNfseFaixaEnvio.getIdentificacaoRequerente().getCpfCnpj().setCnpj("09032577000150");
//        consultarNfseFaixaEnvio.setFaixa(new ConsultarNfseFaixaEnvio.Faixa());
//        consultarNfseFaixaEnvio.getFaixa().setNumeroNfseInicial(new BigInteger("1"));
//        consultarNfseFaixaEnvio.getFaixa().setNumeroNfseFinal(new BigInteger("10"));
//
//        ConsultarNfseFaixaResposta consultarNfseFaixaResposta = nfse203Service.consultarNfseFaixa(consultarNfseFaixaEnvio);
//
//        Assert.assertNotNull(consultarNfseFaixaResposta);
//        Assert.assertNotNull(consultarNfseFaixaResposta.getListaNfse());
//        Assert.assertNotNull(consultarNfseFaixaResposta.getListaNfse().getCompNfse());
//        Assert.assertEquals(10, consultarNfseFaixaResposta.getListaNfse().getCompNfse().size());
//    }
//
//    @Test
//    public void consultarNfseRps() throws Exception {
//        ConsultarNfseRpsEnvio consultarNfseRpsEnvio = new ConsultarNfseRpsEnvio();
//        consultarNfseRpsEnvio.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        consultarNfseRpsEnvio.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        consultarNfseRpsEnvio.getIdentificacaoRequerente().getCpfCnpj().setCnpj("02688986000160");
//        consultarNfseRpsEnvio.getIdentificacaoRequerente().setInscricaoMunicipal("1135520");
//        consultarNfseRpsEnvio.setIdentificacaoRps(new TcIdentificacaoRps());
//        consultarNfseRpsEnvio.getIdentificacaoRps().setNumero(new BigInteger("7947"));
//        consultarNfseRpsEnvio.getIdentificacaoRps().setSerie("2001");
//        consultarNfseRpsEnvio.getIdentificacaoRps().setTipo(new Integer(1).byteValue());
//
//        ConsultarNfseRpsResposta consultarNfseRpsResposta = nfse203Service.consultarNfseRps(consultarNfseRpsEnvio);
//
//        Assert.assertNotNull(consultarNfseRpsResposta);
//        Assert.assertNotNull(consultarNfseRpsResposta.getCompNfse());
//        Assert.assertNotNull(consultarNfseRpsResposta.getCompNfse().getNfse());
//        Assert.assertNotNull(consultarNfseRpsResposta.getCompNfse().getNfse().getInfNfse());
//        Assert.assertEquals(0, consultarNfseRpsResposta.getCompNfse().getNfse().getInfNfse().getNumero().compareTo(new BigInteger("8304")));
//    }
//
//    @Test
//    public void consultarLoteRps() throws ValidacaoNfseWSException, Exception {
//        ConsultarLoteRpsEnvio consultarLoteRpsEnvio = new ConsultarLoteRpsEnvio();
//        consultarLoteRpsEnvio.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        consultarLoteRpsEnvio.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        consultarLoteRpsEnvio.getIdentificacaoRequerente().getCpfCnpj().setCnpj("02688986000160");
//        consultarLoteRpsEnvio.getIdentificacaoRequerente().setInscricaoMunicipal("1135520");
//        consultarLoteRpsEnvio.setNumeroLote(new BigInteger("4000384"));
//
//        ConsultarLoteRpsResposta consultarLoteRpsResposta = nfse203Service.consultarLoteRps(consultarLoteRpsEnvio);
//
//        Assert.assertNotNull(consultarLoteRpsResposta);
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse());
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse().getCompNfse());
//        Assert.assertEquals(46, consultarLoteRpsResposta.getListaNfse().getCompNfse().size());
//    }
//
//    @Test
//    public void consultarServicoPrestado() throws Exception {
//        ConsultarNfseServicoPrestadoEnvio consultarNfseServicoPrestadoEnvio = new ConsultarNfseServicoPrestadoEnvio();
//        consultarNfseServicoPrestadoEnvio.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        consultarNfseServicoPrestadoEnvio.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        consultarNfseServicoPrestadoEnvio.getIdentificacaoRequerente().getCpfCnpj().setCnpj("09032577000150");
//        consultarNfseServicoPrestadoEnvio.getIdentificacaoRequerente().setInscricaoMunicipal("9033572");
//
//        consultarNfseServicoPrestadoEnvio.setPeriodoEmissao(new ConsultarNfseServicoPrestadoEnvio.PeriodoEmissao());
//        consultarNfseServicoPrestadoEnvio.getPeriodoEmissao().setDataInicial(DateUtils.convertDateToXMLGregorianCalendar(DateUtils.getData(1, 10, 2017)));
//        consultarNfseServicoPrestadoEnvio.getPeriodoEmissao().setDataFinal(DateUtils.convertDateToXMLGregorianCalendar(DateUtils.getData(30, 10, 2017)));
//
//        ConsultarNfseServicoPrestadoResposta consultarNfseServicoPrestadoResposta = nfse203Service.consultarServicoPrestado(consultarNfseServicoPrestadoEnvio);
//
//        Assert.assertNotNull(consultarNfseServicoPrestadoResposta);
//        Assert.assertNotNull(consultarNfseServicoPrestadoResposta.getListaNfse());
//        Assert.assertNotNull(consultarNfseServicoPrestadoResposta.getListaNfse().getCompNfse());
//        Assert.assertEquals(3, consultarNfseServicoPrestadoResposta.getListaNfse().getCompNfse().size());
//    }
//
//    @Test
//    public void enviarLote() throws ValidacaoNfseWSException, Exception {
//        EnviarLoteRpsSincronoEnvio lote = new EnviarLoteRpsSincronoEnvio();
//        lote.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        lote.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        lote.getIdentificacaoRequerente().getCpfCnpj().setCnpj("09032577000150");
//        lote.getIdentificacaoRequerente().setInscricaoMunicipal("9033572");
//        lote.getIdentificacaoRequerente().setHomologa(false);
//
//        lote.setLoteRps(new TcLoteRps());
//        lote.getLoteRps().setNumeroLote(new BigInteger("9999"));
//        lote.getLoteRps().setQuantidadeRps(1);
//        lote.getLoteRps().setListaRps(new TcLoteRps.ListaRps());
//
//        TcDeclaracaoPrestacaoServico declaracaoPrestacaoServico = new TcDeclaracaoPrestacaoServico();
//        declaracaoPrestacaoServico.setInfDeclaracaoPrestacaoServico(new TcInfDeclaracaoPrestacaoServico());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setRps(new TcInfRps());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getRps().setIdentificacaoRps(new TcIdentificacaoRps());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().setNumero(new BigInteger("9999"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().setSerie("1");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().setTipo(new Integer(1).byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getRps().setDataEmissao(DateUtils.convertDateToXMLGregorianCalendar(new Date()));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getRps().setStatus(new Integer("1").byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setCompetencia(DateUtils.convertDateToXMLGregorianCalendar(new Date()));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setServico(new TcDadosServico());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setValores(new TcValoresDeclaracaoServico());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorServicos(new BigDecimal("100"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorDeducoes(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorPis(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorCofins(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorInss(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorIr(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorCsll(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setOutrasRetencoes(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorIss(new BigDecimal("5"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setAliquota(new BigDecimal("5"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setDescontoCondicionado(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setDescontoIncondicionado(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setIssRetido(new Integer("2").byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setResponsavelRetencao(new Integer("1").byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setDiscriminacao("Teste");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setCodigoMunicipio(1200401);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setExigibilidadeISS(new Integer(1).byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setMunicipioIncidencia(1200401);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setListaItensServico(new TcDadosServico.ListaItensServico());
//        TcItemServico itemServico = new TcItemServico();
//        itemServico.setItemListaServico("1102");
//        itemServico.setCodigoCnae(0);
//        itemServico.setDescricao("TESTE");
//        itemServico.setTributavel(new Integer(1).byteValue());
//        itemServico.setQuantidade(BigDecimal.ONE);
//        itemServico.setValorUnitario(new BigDecimal("100"));
//        itemServico.setValorDesconto(BigDecimal.ZERO);
//        itemServico.setValorLiquido(new BigDecimal("100"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getListaItensServico().getItemServico().add(itemServico);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setPrestador(new TcIdentificacaoPrestador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setCpfCnpj(new TcCpfCnpj());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().getCpfCnpj().setCnpj("09032577000150");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setInscricaoMunicipal("9033572");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setRazaoSocial("MGA GESTAO PUBLICA LTDA");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setTomador(new TcDadosTomador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setIdentificacaoTomador(new TcIdentificacaoTomador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getIdentificacaoTomador().setCpfCnpj(new TcCpfCnpj());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getIdentificacaoTomador().getCpfCnpj().setCpf("08125170910");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setRazaoSocial("WELLINGTON D O ABDO");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setEndereco(new TcEndereco());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setBairro("PQ INDUSTRIAL");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setCep("87065290");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setCodigoMunicipio(1200401);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setEndereco("AVENIDA PIONEIRO ANTONIO RUIZ SALDANHA");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setNumero("1840");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setComplemento("AP 609 BC 01");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setContato(new TcContato());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getContato().setEmail("wellingtondoabdo@gmail.com");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getContato().setTelefone("44998026110");
//
//        lote.getLoteRps().getListaRps().getRps().add(declaracaoPrestacaoServico);
//
//        EnviarLoteRpsSincronoResposta enviarLoteRpsSincronoResposta = nfse203Service.enviarLoteWsSincrono(lote, "");
//
//        Assert.assertNotNull(enviarLoteRpsSincronoResposta);
//        Assert.assertNotNull(enviarLoteRpsSincronoResposta.getProtocolo());
//        Assert.assertNotNull(enviarLoteRpsSincronoResposta.getListaNfse());
//        Assert.assertNotNull(enviarLoteRpsSincronoResposta.getListaNfse().getCompNfse());
//        Assert.assertEquals(1, enviarLoteRpsSincronoResposta.getListaNfse().getCompNfse().size());
//    }
//
//    @Test
//    public void substituirNfse() throws ValidacaoNfseWSException, DatatypeConfigurationException {
//        SubstituirNfseEnvio substituirNfseEnvio = new SubstituirNfseEnvio();
//        substituirNfseEnvio.setIdentificacaoRequerente(new TcIdentificacaoRequerente());
//        substituirNfseEnvio.getIdentificacaoRequerente().setCpfCnpj(new TcCpfCnpj());
//        substituirNfseEnvio.getIdentificacaoRequerente().getCpfCnpj().setCnpj("09032577000150");
//        substituirNfseEnvio.getIdentificacaoRequerente().setInscricaoMunicipal("9033572");
//        substituirNfseEnvio.getIdentificacaoRequerente().setHomologa(false);
//
//        substituirNfseEnvio.setSubstituicaoNfse(new SubstituirNfseEnvio.SubstituicaoNfse());
//        substituirNfseEnvio.getSubstituicaoNfse().setPedido(new TcPedidoCancelamento());
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().setInfPedidoCancelamento(new TcInfPedidoCancelamento());
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().getInfPedidoCancelamento().setIdentificacaoNfse(new TcIdentificacaoNfse());
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setCpfCnpj(new TcCpfCnpj());
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().getCpfCnpj().setCnpj("09032577000150");
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setInscricaoMunicipal("9033572");
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setNumero(new BigInteger("3"));
//        substituirNfseEnvio.getSubstituicaoNfse().getPedido().getInfPedidoCancelamento().setCodigoCancelamento(new Integer(1).byteValue());
//
//        TcDeclaracaoPrestacaoServico declaracaoPrestacaoServico = new TcDeclaracaoPrestacaoServico();
//        declaracaoPrestacaoServico.setInfDeclaracaoPrestacaoServico(new TcInfDeclaracaoPrestacaoServico());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setCompetencia(DateUtils.convertDateToXMLGregorianCalendar(new Date()));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setServico(new TcDadosServico());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setValores(new TcValoresDeclaracaoServico());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorServicos(new BigDecimal("100"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorDeducoes(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorPis(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorCofins(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorInss(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorIr(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorCsll(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setOutrasRetencoes(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setValorIss(new BigDecimal("5"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setAliquota(new BigDecimal("5"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setDescontoCondicionado(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getValores().setDescontoIncondicionado(BigDecimal.ZERO);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setIssRetido(new Integer("2").byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setResponsavelRetencao(new Integer("1").byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setDiscriminacao("Teste");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setCodigoMunicipio(1200401);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setExigibilidadeISS(new Integer(1).byteValue());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setMunicipioIncidencia(1200401);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setCodigoPais("76");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().setListaItensServico(new TcDadosServico.ListaItensServico());
//        TcItemServico itemServico = new TcItemServico();
//        itemServico.setItemListaServico("101");
//        itemServico.setCodigoCnae(0);
//        itemServico.setDescricao("TESTE");
//        itemServico.setTributavel(new Integer(1).byteValue());
//        itemServico.setQuantidade(BigDecimal.ONE);
//        itemServico.setValorUnitario(new BigDecimal("100"));
//        itemServico.setValorDesconto(BigDecimal.ZERO);
//        itemServico.setValorLiquido(new BigDecimal("100"));
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getServico().getListaItensServico().getItemServico().add(itemServico);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setPrestador(new TcIdentificacaoPrestador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setCpfCnpj(new TcCpfCnpj());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().getCpfCnpj().setCnpj("09032577000150");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setInscricaoMunicipal("9033572");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setIdentificacaoPrestador(new TcIdentificacaoPrestador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().getIdentificacaoPrestador().setCpfCnpj(new TcCpfCnpj());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().getIdentificacaoPrestador().getCpfCnpj().setCnpj("09032577000150");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().getIdentificacaoPrestador().setInscricaoMunicipal("9033572");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getPrestador().setRazaoSocial("MGA GESTAO PUBLICA LTDA");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().setTomador(new TcDadosTomador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setIdentificacaoTomador(new TcIdentificacaoTomador());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getIdentificacaoTomador().setCpfCnpj(new TcCpfCnpj());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getIdentificacaoTomador().getCpfCnpj().setCpf("08125170910");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setRazaoSocial("WELLINGTON D O ABDO");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setEndereco(new TcEndereco());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setBairro("PQ INDUSTRIAL");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setCep("87065290");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setCodigoMunicipio(1200401);
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setEndereco("AVENIDA PIONEIRO ANTONIO RUIZ SALDANHA");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setNumero("1840");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getEndereco().setComplemento("AP 609 BC 01");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().setContato(new TcContato());
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getContato().setEmail("wellingtondoabdo@gmail.com");
//        declaracaoPrestacaoServico.getInfDeclaracaoPrestacaoServico().getTomador().getContato().setTelefone("44998026110");
//
//        substituirNfseEnvio.getSubstituicaoNfse().setRps(declaracaoPrestacaoServico);
//
//        SubstituirNfseResposta substituirNfseResposta = nfse203Service.substituirNnfseWs(substituirNfseEnvio);
//
//        Assert.assertNotNull(substituirNfseResposta);
//        Assert.assertNotNull(substituirNfseResposta.getRetSubstituicao().getNfseSubstituidora().getCompNfse());
//    }
}
