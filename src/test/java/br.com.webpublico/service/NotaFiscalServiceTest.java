package br.com.webpublico.service;

import br.com.webpublico.ApplicationContextTest;

public class NotaFiscalServiceTest extends ApplicationContextTest {
//
//    @Autowired
//    private NotaFiscalService notaFiscalService;
//    @Autowired
//    private CadastroEconomicoService cadastroEconomicoService;
//    @Autowired
//    private PessoaService pessoaService;
//    @Autowired
//    private ServicoService servicoService;
//    @Autowired
//    private CidadeService cidadeService;
//    @Autowired
//    private PaisService paisService;
//
//    private NotaFiscalNfseDTO notaFiscalDefault() {
//        NotaFiscalNfseDTO dto = notaFiscalService.criarNotaFiscal();
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setPrestadoNoPais(Boolean.TRUE);
//        itemDeclaracaoServico.setMunicipio(cidadeService.findByCodigoIBGE("1200401"));
//
//        if (dto.getDeclaracaoPrestacaoServico().getTributosFederais() == null) {
//            dto.getDeclaracaoPrestacaoServico().setTributosFederais(new TributosFederaisNfseDTO());
//        }
//        return dto;
//    }
//
////    @Test
//    public void validarValoresNotaFiscal() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1076507"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("07790883000120").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1401"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("700.50"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("3"));
//        itemDeclaracaoServico.setDescontosIncondicionados(new BigDecimal("0.50"));
//        itemDeclaracaoServico.setDescontosCondicionados(new BigDecimal("0.55"));
//        itemDeclaracaoServico.setDeducoes(new BigDecimal("3.58"));
//
//        TributosFederaisNfseDTO tributosFederais = dto.getDeclaracaoPrestacaoServico().getTributosFederais();
//        tributosFederais.setInss(new BigDecimal("5.8"));
//        tributosFederais.setPis(new BigDecimal("4.55"));
//        tributosFederais.setCofins(new BigDecimal("21"));
//        tributosFederais.setCsll(new BigDecimal("7"));
//        tributosFederais.setIrrf(new BigDecimal("1.78"));
//        tributosFederais.setOutrasRetencoes(new BigDecimal("3.5"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("2101.50").compareTo(dto.getTotalServicos()));
//        Assert.assertEquals(0, new BigDecimal("2100.45").compareTo(dto.getTotalNota()));
//        Assert.assertEquals(0, new BigDecimal("2097.42").compareTo(dto.getBaseCalculo()));
//        Assert.assertEquals(0, new BigDecimal("2056.82").compareTo(dto.getValorLiquido()));
//    }
//
//
////    @Test
//    public void createNotaFiscal() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1076507"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("07790883000120").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1401"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("700.50"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("3"));
//        itemDeclaracaoServico.setDescontosIncondicionados(new BigDecimal("0.50"));
//        itemDeclaracaoServico.setDescontosCondicionados(new BigDecimal("0.55"));
//        itemDeclaracaoServico.setDeducoes(new BigDecimal("3.58"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertNotNull(dto.getId());
//    }
//
////    @Test
//    public void imprimirNotaFiscal() throws IOException, JRException {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1076507"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("07790883000120").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1401"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("700.50"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("3"));
//        itemDeclaracaoServico.setDescontosIncondicionados(new BigDecimal("0.50"));
//        itemDeclaracaoServico.setDescontosCondicionados(new BigDecimal("0.55"));
//        itemDeclaracaoServico.setDeducoes(new BigDecimal("3.58"));
//
//        dto = notaFiscalService.save(dto);
//
//        ByteArrayOutputStream bytesPdf = notaFiscalService.getBytesPdf(dto.getId());
//
//        Assert.assertNotNull(bytesPdf);
//        Assert.assertNotEquals(0, bytesPdf.toByteArray().length);
//    }
//
////    @Test
//    public void naturezaOperacaoISSQNARecolher() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1000101"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("04600060000104").getDadosPessoais());
//        dto.setSubstitutoTributario(false);
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("ISS de responsabilidade do: Prestador de Serviço"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Serviço tributado no município: RIO BRANCO"));
//    }
//
//    @Test
//    public void naturezaOperacaoRetencaoISSQN() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.RETENCAO);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1000101"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//        dto.setSubstitutoTributario(true);
//        dto.getDeclaracaoPrestacaoServico().setIssRetido(true);
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("ISS de responsabilidade do: Tomador de Serviço"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Serviço tributado no município: RIO BRANCO"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Operação com retenção de ISS por Substituição Tributária"));
//    }
//
//    @Test
//    public void naturezaOperacaoSimplesNacional() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.SIMPLES_NACIONAL);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1001000"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("04600060000104").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("ISS de responsabilidade do: Prestador de Serviço"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Serviço tributado no município: RIO BRANCO"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("ISSQN a ser calculado pela Tabela da LC 123/Simples Nacional e pago na guia DAS/Simples"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL"));
//    }
//
//    @Test
//    public void naturezaOperacaoRetencaoSimplesNacional() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.RETENCAO_SIMPLES_NACIONAL);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1001000"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//        dto.setSubstitutoTributario(true);
//        dto.getDeclaracaoPrestacaoServico().setIssRetido(true);
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("ISS de responsabilidade do: Tomador de Serviço"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Serviço tributado no município: RIO BRANCO"));
////        IMPLEMENTAR
////        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Operação com retenção de ISS por Substituição Tributária"));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL"));
//    }
//
//    @Test
//    public void naturezaOperacaoNaoIncidencia() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.NAO_INCIDENCIA);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1044125"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Operação não gera valor de ISSQN. Contribuinte Fixo, MEI ou Estimado"));
//    }
//
//    @Test
//    public void naturezaOperacaoImunidade() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.IMUNIDADE);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1048813"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Operação não gera valor de ISSQN. Contribuinte no regime de recolhimento imune"));
//    }
//
//    @Test
//    public void naturezaOperacaoIsento() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.ISENCAO);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1116886"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1701"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Operação não gera valor de ISSQN. Contribuinte no regime de recolhimento isento"));
//    }
//
//    @Test
//    public void naturezaOperacaoTributacaoForaMunicipio() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.TRIBUTACAO_FORA_MUNICIPIO);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1000101"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1705"));
//        itemDeclaracaoServico.setMunicipio(cidadeService.findByCodigoIBGE("1200054"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Serviço tributado no município: ASSIS BRASIL"));
//    }
//
//
//    @Test
//    public void naturezaOperacaoExportacao() {
//        NotaFiscalNfseDTO dto = notaFiscalDefault();
//        dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.EXPORTACAO);
//        dto.setPrestador(cadastroEconomicoService.findByInscricaoMunicipal("1000101"));
//
//        dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
//        dto.setTomador(new TomadorServicoDTO());
//        dto.getTomador().setPrestador(dto.getPrestador());
//        dto.getTomador().setDadosPessoais(pessoaService.findByCpfCnpj("33530486006755").getDadosPessoais());
//
//        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = dto.getDeclaracaoPrestacaoServico().getItens().get(0);
//        itemDeclaracaoServico.setServico(servicoService.findByCodigo("1705"));
//        itemDeclaracaoServico.setPrestadoNoPais(false);
//        itemDeclaracaoServico.setPais(paisService.findByCodigo("4"));
//        itemDeclaracaoServico.setAliquotaServico(itemDeclaracaoServico.getServico().getAliquota());
//        itemDeclaracaoServico.setValorServico(new BigDecimal("100"));
//        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
//
//
//        dto = notaFiscalService.save(dto);
//
//        Assert.assertEquals(0, new BigDecimal("5").compareTo(dto.getIssCalculado()));
//        Assert.assertEquals(0, BigDecimal.ZERO.compareTo(dto.getIssPagar()));
//        Assert.assertTrue(dto.getInformacoesAdicionais().contains("Serviço tributado no país: Afeganistão"));
//    }
}

