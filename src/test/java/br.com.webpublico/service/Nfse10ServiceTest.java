package br.com.webpublico.service;

import br.com.webpublico.ApplicationContextTest;

public class Nfse10ServiceTest extends ApplicationContextTest {
//
//    @Autowired
//    private Nfse10Service nfse10Service;
//    @Autowired
//    private CadastroEconomicoService cadastroEconomicoService;
//    @Autowired
//    private CidadeService cidadeService;
//    @Autowired
//    private ServicoService servicoService;
//    @Autowired
//    private PessoaService pessoaService;
//
//    @Test
//    public void enviarLoteRps() throws ValidacaoNfseWSException, InterruptedException {
//        EnviarLoteRpsEnvio enviarLoteRpsEnvio = new EnviarLoteRpsEnvio();
//
//        enviarLoteRpsEnvio.setLoteRps(new TcLoteRps());
//        enviarLoteRpsEnvio.getLoteRps().setId("9999");
//        enviarLoteRpsEnvio.getLoteRps().setCnpj("09032577000150");
//        enviarLoteRpsEnvio.getLoteRps().setInscricaoMunicipal("9033572");
//        enviarLoteRpsEnvio.getLoteRps().setNumeroLote(new BigInteger("9999"));
//        enviarLoteRpsEnvio.getLoteRps().setQuantidadeRps(1);
//        enviarLoteRpsEnvio.getLoteRps().setListaRps(new TcLoteRps.ListaRps());
//        enviarLoteRpsEnvio.getLoteRps().setVersao("1.0");
//
//        TcLoteRps.ListaRps listaRps = enviarLoteRpsEnvio.getLoteRps().getListaRps();
//        TcRps tcRps = new TcRps();
//        tcRps.setInfRps(new TcInfRps());
//        tcRps.getInfRps().setIdentificacaoRps(new TcIdentificacaoRps());
//        tcRps.getInfRps().getIdentificacaoRps().setNumero(new BigInteger("9999"));
//        tcRps.getInfRps().getIdentificacaoRps().setSerie("1");
//        tcRps.getInfRps().getIdentificacaoRps().setTipo(new Long(1).byteValue());
//        tcRps.getInfRps().setDataEmissao(DateUtils.convertDateToXMLGregorianCalendar(new Date()));
//        tcRps.getInfRps().setNaturezaOperacao(new Long(1).byteValue());
//        tcRps.getInfRps().setOptanteSimplesNacional(new Long(2).byteValue());
//        tcRps.getInfRps().setIncentivadorCultural(new Long(2).byteValue());
//        tcRps.getInfRps().setStatus(new Long(1).byteValue());
//
//        tcRps.getInfRps().setServico(new TcDadosServico());
//        tcRps.getInfRps().getServico().setValores(new TcValores());
//        tcRps.getInfRps().getServico().getValores().setValorServicos(new BigDecimal("100"));
//        tcRps.getInfRps().getServico().getValores().setIssRetido(new Long(2).byteValue());
//        tcRps.getInfRps().getServico().getValores().setValorIss(new BigDecimal("5"));
//        tcRps.getInfRps().getServico().getValores().setBaseCalculo(new BigDecimal("100"));
//        tcRps.getInfRps().getServico().getValores().setAliquota(new BigDecimal("0.05"));
//        tcRps.getInfRps().getServico().getValores().setValorLiquidoNfse(new BigDecimal("100"));
//        tcRps.getInfRps().getServico().getValores().setDescontoIncondicionado(BigDecimal.ZERO);
//        tcRps.getInfRps().getServico().setItemListaServico("803");
//        tcRps.getInfRps().getServico().setCodigoCnae(8531700);
//        tcRps.getInfRps().getServico().setCodigoTributacaoMunicipio("8.03");
//        tcRps.getInfRps().getServico().setDiscriminacao("Teste");
//        tcRps.getInfRps().getServico().setCodigoMunicipio(1200401);
//
//        tcRps.getInfRps().setPrestador(new TcIdentificacaoPrestador());
//        tcRps.getInfRps().getPrestador().setCnpj("09032577000150");
//        tcRps.getInfRps().getPrestador().setInscricaoMunicipal("9033572");
//
//        tcRps.getInfRps().setTomador(new TcDadosTomador());
//        tcRps.getInfRps().getTomador().setIdentificacaoTomador(new TcIdentificacaoTomador());
//        tcRps.getInfRps().getTomador().getIdentificacaoTomador().setCpfCnpj(new TcCpfCnpj());
//        tcRps.getInfRps().getTomador().getIdentificacaoTomador().getCpfCnpj().setCpf("08125170910");
//        tcRps.getInfRps().getTomador().setRazaoSocial("WELLINGTON D O ABDO");
//        tcRps.getInfRps().getTomador().setEndereco(new TcEndereco());
//        tcRps.getInfRps().getTomador().getEndereco().setEndereco("AV PIONEIRO ANTONIO RUIZ SALDANHA");
//        tcRps.getInfRps().getTomador().getEndereco().setNumero("1840");
//        tcRps.getInfRps().getTomador().getEndereco().setBairro("PQ INDUSTRIAL");
//        tcRps.getInfRps().getTomador().getEndereco().setCodigoMunicipio(4115200);
//        tcRps.getInfRps().getTomador().getEndereco().setUf("PR");
//        tcRps.getInfRps().getTomador().getEndereco().setCep(87065290);
//
//        tcRps.getInfRps().getTomador().setContato(new TcContato());
//        tcRps.getInfRps().getTomador().getContato().setEmail("wellingtondoabdo@gmail.com");
//        tcRps.getInfRps().getTomador().getContato().setTelefone("44998026110");
//
//        listaRps.getRps().add(tcRps);
//
//        EnviarLoteRpsResposta enviarLoteRpsResposta = nfse10Service.enviarLoteWs(enviarLoteRpsEnvio, "");
//
//        Assert.assertNotNull(enviarLoteRpsResposta);
//        Assert.assertNotNull(enviarLoteRpsResposta.getListaMensagemRetorno());
//        Assert.assertNotNull(enviarLoteRpsResposta.getListaMensagemRetorno().getMensagemRetorno());
//        Assert.assertTrue(enviarLoteRpsResposta.getListaMensagemRetorno().getMensagemRetorno().get(0).getCodigo().equals("000"));
//    }
//
//    @Test
//    public void consultarLoteRps() throws ValidacaoNfseWSException, Exception {
//        ConsultarLoteRpsEnvio consultarLoteRpsEnvio = new ConsultarLoteRpsEnvio();
//        consultarLoteRpsEnvio.setPrestador(new TcIdentificacaoPrestador());
//        consultarLoteRpsEnvio.getPrestador().setCnpj("02688986000160");
//        consultarLoteRpsEnvio.getPrestador().setInscricaoMunicipal("1135520");
//        consultarLoteRpsEnvio.setProtocolo("Fe5b-DAA0-7FDE-f3dE");
//
//        ConsultarLoteRpsResposta consultarLoteRpsResposta = nfse10Service.consultarLoteRps(consultarLoteRpsEnvio);
//
//        Assert.assertNotNull(consultarLoteRpsResposta);
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse());
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse().getCompNfse());
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse().getCompNfse().get(0));
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse().getCompNfse().get(0).getNfse());
//        Assert.assertNotNull(consultarLoteRpsResposta.getListaNfse().getCompNfse().get(0).getNfse().getInfNfse());
//        Assert.assertEquals(46, consultarLoteRpsResposta.getListaNfse().getCompNfse().size());
//    }
//
//
//    @Test
//    public void cancelarNfse() throws ValidacaoNfseWSException {
//        CancelarNfseEnvio cancelarNfseEnvio = new CancelarNfseEnvio();
//        cancelarNfseEnvio.setPedido(new TcPedidoCancelamento());
//        cancelarNfseEnvio.getPedido().setInfPedidoCancelamento(new TcInfPedidoCancelamento());
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().setCodigoCancelamento("1");
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().setIdentificacaoNfse(new TcIdentificacaoNfse());
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setNumero(new BigInteger("3"));
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setCnpj("09032577000150");
//        cancelarNfseEnvio.getPedido().getInfPedidoCancelamento().getIdentificacaoNfse().setInscricaoMunicipal("9033572");
//
//        CancelarNfseResposta resposta = nfse10Service.cancelar(cancelarNfseEnvio);
//
//        Assert.assertNotNull(resposta);
//        Assert.assertNotNull(resposta.getRetCancelamento());
//    }
//
//    @Test
//    public void consultarNfse() throws ValidacaoNfseWSException, Exception {
//        ConsultarNfseEnvio consultarNfseEnvio = new ConsultarNfseEnvio();
//        consultarNfseEnvio.setPrestador(new TcIdentificacaoPrestador());
//        consultarNfseEnvio.getPrestador().setInscricaoMunicipal("9033572");
//        consultarNfseEnvio.getPrestador().setCnpj("09032577000150");
//        consultarNfseEnvio.setNumeroNfse(new BigInteger("3"));
//
//        ConsultarNfseResposta consultarNfseResposta = nfse10Service.consultarNfse(consultarNfseEnvio);
//
//        Assert.assertNotNull(consultarNfseResposta);
//        Assert.assertNotNull(consultarNfseResposta.getListaNfse());
//        Assert.assertNotNull(consultarNfseResposta.getListaNfse().getCompNfse());
//        Assert.assertNotNull(consultarNfseResposta.getListaNfse().getCompNfse().getNfse());
//        Assert.assertNotNull(consultarNfseResposta.getListaNfse().getCompNfse().getNfse().getInfNfse());
//        Assert.assertEquals(0, consultarNfseResposta.getListaNfse().getCompNfse().getNfse().getInfNfse().getNumero().compareTo(new BigInteger("3")));
//    }
//
//    @Test
//    public void consultarNfsePorRps() throws Exception, ValidacaoNfseWSException {
//        ConsultarNfseRpsEnvio consultarNfseRpsEnvio = new ConsultarNfseRpsEnvio();
//        consultarNfseRpsEnvio.setPrestador(new TcIdentificacaoPrestador());
//        consultarNfseRpsEnvio.getPrestador().setCnpj("02688986000160");
//        consultarNfseRpsEnvio.getPrestador().setInscricaoMunicipal("1135520");
//        consultarNfseRpsEnvio.setIdentificacaoRps(new TcIdentificacaoRps());
//        consultarNfseRpsEnvio.getIdentificacaoRps().setNumero(new BigInteger("7947"));
//        consultarNfseRpsEnvio.getIdentificacaoRps().setSerie("2001");
//
//        ConsultarNfseRpsResposta consultarNfseRpsResposta = nfse10Service.consultarNfseRps(consultarNfseRpsEnvio);
//
//        Assert.assertNotNull(consultarNfseRpsResposta);
//        Assert.assertNotNull(consultarNfseRpsResposta.getCompNfse());
//        Assert.assertNotNull(consultarNfseRpsResposta.getCompNfse().getNfse());
//        Assert.assertNotNull(consultarNfseRpsResposta.getCompNfse().getNfse().getInfNfse());
//        Assert.assertEquals(0, consultarNfseRpsResposta.getCompNfse().getNfse().getInfNfse().getNumero().compareTo(new BigInteger("8304")));
//    }
//
//    @Test
//    public void consultarSituacaoLoteRps() throws ValidacaoNfseWSException, DatatypeConfigurationException {
//        ConsultarSituacaoLoteRpsEnvio consultarSituacaoLoteRpsEnvio = new ConsultarSituacaoLoteRpsEnvio();
//        consultarSituacaoLoteRpsEnvio.setPrestador(new TcIdentificacaoPrestador());
//        consultarSituacaoLoteRpsEnvio.getPrestador().setCnpj("02688986000160");
//        consultarSituacaoLoteRpsEnvio.getPrestador().setInscricaoMunicipal("1135520");
//        consultarSituacaoLoteRpsEnvio.setProtocolo("Fe5b-DAA0-7FDE-f3dE");
//
//        ConsultarSituacaoLoteRpsResposta consultarSituacaoLoteRpsResposta = nfse10Service.consultarLoteRps(consultarSituacaoLoteRpsEnvio);
//
//        Assert.assertNotNull(consultarSituacaoLoteRpsResposta);
//        Assert.assertTrue(consultarSituacaoLoteRpsResposta.getSituacao().equals(new Long(3).byteValue()));
//    }
//
}
