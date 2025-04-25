package br.com.webpublico.domain.dto.util;

import br.com.webpublico.BigDecimalUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.ResponsavelRetencaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoRpsNfseDTO;
import br.com.webpublico.domain.dto.exception.TipoValidacao;
import br.com.webpublico.domain.dto.exception.ValidacaoNfseWSException;
import br.com.webpublico.domain.dto.nfse10.*;
import br.com.webpublico.domain.dto.nfse12.TcDeclaracaoPrestacaoServico;
import br.com.webpublico.domain.dto.nfse12.TcIdentificacaoRequerente;
import br.com.webpublico.domain.dto.nfse12.TcItemServico;
import br.com.webpublico.domain.dto.nfse203.EnviarLoteRpsSincronoEnvio;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LoteRpsBuild {

    public static final String ABRASF_VERSAO_1_0 = "1.0";
    public static final String ABRASF_VERSAO_1_2 = "1.2";
    public static final String ABRASF_VERSAO_2_03 = "2.03";
    public static byte tcSim = new Integer(1).byteValue();
    public static byte tcNao = new Integer(2).byteValue();

    public static String getInscricaoMunicipal(TcLoteRps tcLoteRps) {
        if (tcLoteRps == null) return null;
        return !Strings.isNullOrEmpty(tcLoteRps.getCnpj()) ?
                tcLoteRps.getCnpj() :
                tcLoteRps.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(TcIdentificacaoPrestador tcIdentificacaoPrestador) {
        if (tcIdentificacaoPrestador == null) return null;
        return !Strings.isNullOrEmpty(tcIdentificacaoPrestador.getCnpj()) ?
                tcIdentificacaoPrestador.getCnpj() :
                tcIdentificacaoPrestador.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(TcIdentificacaoTomador tcIdentificacaoTomador) {
        if (tcIdentificacaoTomador == null ||
                Strings.isNullOrEmpty(tcIdentificacaoTomador.getInscricaoMunicipal())) return null;
        return tcIdentificacaoTomador.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(tcIdentificacaoTomador.getCpfCnpj().getCnpj()) ?
                tcIdentificacaoTomador.getCpfCnpj().getCnpj() :
                tcIdentificacaoTomador.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(TcIdentificacaoNfse tcIdentificacaoNfse) {
        if (tcIdentificacaoNfse == null) return null;
        return !Strings.isNullOrEmpty(tcIdentificacaoNfse.getCnpj()) ?
                tcIdentificacaoNfse.getCnpj() :
                tcIdentificacaoNfse.getInscricaoMunicipal();
    }

    public static LoteRpsNfseDTO criarLoteRps10(TcLoteRps loteRps) throws ValidacaoNfseWSException {
        if (loteRps.getQuantidadeRps() != loteRps.getListaRps().getRps().size()) {
            throw new ValidacaoNfseWSException(TipoValidacao.E069);
        }

        LoteRpsNfseDTO loteDto = new LoteRpsNfseDTO();
        loteDto.setHomologacao(false);
        loteDto.setNumero(loteRps.getNumeroLote().intValue());
        loteDto.setDataRecebimento(new Date());
        loteDto.setVersaoAbrasf(ABRASF_VERSAO_1_0);

        for (TcRps rps : loteRps.getListaRps().getRps()) {
            RpsNfseDTO dto = new RpsNfseDTO();
            dto.setNumero(rps.getInfRps().getIdentificacaoRps().getNumero().toString());
            dto.setSerie(rps.getInfRps().getIdentificacaoRps().getSerie());
            dto.setTipoRps(TipoRpsNfseDTO.findByCodigo(rps.getInfRps().getIdentificacaoRps().getTipo()));
            dto.setDataEmissao(rps.getInfRps().getDataEmissao().toGregorianCalendar().getTime());
            dto.setIssCalculado(rps.getInfRps().getServico().getValores().getValorIss());
            dto.setIssPagar(rps.getInfRps().getServico().getValores().getValorIss());
            dto.setCompetencia(rps.getInfRps().getDataEmissao().toGregorianCalendar().getTime());

            TcDadosServico servico = rps.getInfRps().getServico();

            TributosFederaisNfseDTO tributosFederaisDto = new TributosFederaisNfseDTO();
            tributosFederaisDto.setPis(servico.getValores().getValorPis());
            tributosFederaisDto.setCofins(servico.getValores().getValorCofins());
            tributosFederaisDto.setIrrf(servico.getValores().getValorIr());
            tributosFederaisDto.setCsll(servico.getValores().getValorCsll());
            tributosFederaisDto.setInss(servico.getValores().getValorInss());
            tributosFederaisDto.setOutrasRetencoes(servico.getValores().getOutrasRetencoes());
            dto.setTributosFederais(tributosFederaisDto);

            dto.setTotalServicos(servico.getValores().getValorServicos());
            dto.setDeducoesLegais(servico.getValores().getValorDeducoes());
            dto.setDescontosCondicionais(servico.getValores().getDescontoCondicionado());
            dto.setDescontosIncondicionais(servico.getValores().getDescontoIncondicionado());
            dto.setTotalNota(dto.getTotalServicos().subtract(dto.getDescontos()));
            dto.setRetencoesFederais(BigDecimalUtils.add(servico.getValores().getValorPis(), servico.getValores().getValorCofins(), servico.getValores().getValorCsll(),
                    servico.getValores().getValorIr(), servico.getValores().getValorInss(), servico.getValores().getOutrasRetencoes()));

            dto.setIssRetido(servico.getValores().getIssRetido() == tcSim);

            dto.setDescriminacaoServico(servico.getDiscriminacao());
            dto.setNaturezaOperacao(ExigibilidadeNfseDTO.getByCodigoV1((int) rps.getInfRps().getNaturezaOperacao()));

            dto.setValorLiquido(BigDecimal.ZERO);

            ItemDeclaracaoServicoNfseDTO itemDto = new ItemDeclaracaoServicoNfseDTO();
            itemDto.setMunicipio(new MunicipioNfseDTO());
            itemDto.getMunicipio().setCodigoIbge(new Integer(servico.getCodigoMunicipio()).toString());
            itemDto.setPais(new PaisNfseDTO());

            itemDto.setValorServico(servico.getValores().getValorServicos());
            itemDto.setDeducoes(servico.getValores().getValorDeducoes());
            itemDto.setIss(servico.getValores().getValorIss());
            if (servico.getValores().getAliquota() != null
                    && servico.getValores().getAliquota().compareTo(new BigDecimal("0.1")) < 0) {
                servico.getValores().setAliquota(servico.getValores().getAliquota().multiply(new BigDecimal("100")));
            }
            itemDto.setAliquotaServico(servico.getValores().getAliquota());
            itemDto.setDescontosCondicionados(servico.getValores().getDescontoCondicionado());
            itemDto.setDescontosIncondicionados(servico.getValores().getDescontoIncondicionado());
            dto.setValorLiquido(servico.getValores().getValorLiquidoNfse());

            ServicoNfseDTO servicoDto = new ServicoNfseDTO();
            servicoDto.setCodigo(servico.getItemListaServico());
            itemDto.setServico(servicoDto);

            CnaeNfseDTO cnaeDto = new CnaeNfseDTO();
            cnaeDto.setCodigo(servico.getCodigoCnae() + "");
            itemDto.setCnae(cnaeDto);

            MunicipioNfseDTO municipioDto = new MunicipioNfseDTO();
            municipioDto.setCodigoIbge(servico.getCodigoMunicipio() + "");
            itemDto.setMunicipio(municipioDto);

            itemDto.setDescricao(servico.getDiscriminacao());
            itemDto.setQuantidade(BigDecimal.ONE);

            itemDto.setValorServico(servico.getValores().getValorServicos());
            itemDto.setBaseCalculo(servico.getValores().getBaseCalculo());

            itemDto.setPrestadoNoPais(true);

            dto.getItens().add(itemDto);


            PrestadorServicoNfseDTO prestadorDto = new PrestadorServicoNfseDTO();
            prestadorDto.setInscricaoMunicipal(getInscricaoMunicipal(rps.getInfRps().getPrestador()));
            dto.setPrestador(prestadorDto);

            dto.setDadosPessoaisPrestador(new DadosPessoaisNfseDTO());
            if (rps.getInfRps().getPrestador().getCnpj() != null) {
                dto.getDadosPessoaisPrestador().setCpfCnpj(rps.getInfRps().getPrestador().getCnpj());
            }
            if (rps.getInfRps().getTomador() != null) {
                TcDadosTomador tomador = rps.getInfRps().getTomador();
                DadosPessoaisNfseDTO dadosTomadorDto = new DadosPessoaisNfseDTO();
                if (tomador != null && tomador.getIdentificacaoTomador() != null) {
                    if (tomador.getIdentificacaoTomador().getCpfCnpj().getCnpj() != null) {
                        dadosTomadorDto.setCpfCnpj(tomador.getIdentificacaoTomador().getCpfCnpj().getCnpj());
                    } else {
                        dadosTomadorDto.setCpfCnpj(tomador.getIdentificacaoTomador().getCpfCnpj().getCpf());
                    }
                    dadosTomadorDto.setInscricaoMunicipal(getInscricaoMunicipal(tomador.getIdentificacaoTomador()));
                    dadosTomadorDto.setNomeRazaoSocial(tomador.getRazaoSocial());
                    if (tomador.getEndereco() != null) {
                        dadosTomadorDto.setLogradouro(tomador.getEndereco().getEndereco());
                        dadosTomadorDto.setNumero(tomador.getEndereco().getNumero());
                        dadosTomadorDto.setComplemento(tomador.getEndereco().getComplemento());
                        dadosTomadorDto.setBairro(tomador.getEndereco().getBairro());
                        if (tomador.getEndereco().getCodigoMunicipio() != null) {
                            MunicipioNfseDTO municipioNfseDTO = new MunicipioNfseDTO();
                            municipioNfseDTO.setCodigoIbge(tomador.getEndereco().getCodigoMunicipio() + "");
                            dadosTomadorDto.setMunicipio(municipioNfseDTO);
                        }
                        if (tomador.getEndereco().getCodigoPais() != null) {
                            PaisNfseDTO paisNfseDTO = new PaisNfseDTO();
                            paisNfseDTO.setCodigo(tomador.getEndereco().getCodigoPais() + "");
                            dadosTomadorDto.setPais(paisNfseDTO);
                        }
                        dadosTomadorDto.setBairro(tomador.getEndereco().getBairro());
                        dadosTomadorDto.setCep(String.valueOf(tomador.getEndereco().getCep()));
                    }
                    if (tomador.getContato() != null) {
                        dadosTomadorDto.setTelefone(tomador.getContato().getTelefone());
                        dadosTomadorDto.setEmail(tomador.getContato().getEmail());
                    }
                }
                dto.setDadosPessoaisTomador(dadosTomadorDto);
            }

            if (rps.getInfRps().getIntermediarioServico() != null) {
                dto.setIntermediario(new IntermediarioServicoNfseDTO());
                dto.getIntermediario().setCpfCnpj(rps.getInfRps().getIntermediarioServico().getCpfCnpj().toString());
            }

            if (rps.getInfRps().getContrucaoCivil() != null) {
                ConstrucaoCivilNfseDTO construcaoCivil = new ConstrucaoCivilNfseDTO();
                construcaoCivil.setArt(rps.getInfRps().getContrucaoCivil().getArt());
                construcaoCivil.setCodigoObra(rps.getInfRps().getContrucaoCivil().getCodigoObra());
                dto.setConstrucaoCivil(construcaoCivil);
            }

            loteDto.getListaRps().add(dto);
        }
        return loteDto;
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse12.TcIdentificacaoRequerente tcIdentificacaoRequerente) {
        if (tcIdentificacaoRequerente == null) return null;
        return tcIdentificacaoRequerente.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(tcIdentificacaoRequerente.getCpfCnpj().getCnpj()) ?
                tcIdentificacaoRequerente.getCpfCnpj().getCnpj() :
                tcIdentificacaoRequerente.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse12.TcIdentificacaoPrestador tcIdentificacaoPrestador) {
        if (tcIdentificacaoPrestador == null) return null;
        return tcIdentificacaoPrestador.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(tcIdentificacaoPrestador.getCpfCnpj().getCnpj()) ?
                tcIdentificacaoPrestador.getCpfCnpj().getCnpj() :
                tcIdentificacaoPrestador.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse12.TcIdentificacaoTomador tcIdentificacaoTomador) {
        if (tcIdentificacaoTomador == null || Strings.isNullOrEmpty(tcIdentificacaoTomador.getInscricaoMunicipal())) return null;
        return tcIdentificacaoTomador.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(tcIdentificacaoTomador.getCpfCnpj().getCnpj()) ?
                tcIdentificacaoTomador.getCpfCnpj().getCnpj() :
                tcIdentificacaoTomador.getInscricaoMunicipal();
    }

    public static LoteRpsNfseDTO criarLoteRps12(TcIdentificacaoRequerente requerente,
                                                br.com.webpublico.domain.dto.nfse12.TcLoteRps loteRps) throws ValidacaoNfseWSException {
        if (loteRps.getQuantidadeRps() != loteRps.getListaRps().getDeclaracaoPrestacaoServico().size()) {
            throw new ValidacaoNfseWSException(TipoValidacao.E069);
        }

        LoteRpsNfseDTO loteDto = new LoteRpsNfseDTO();
        UserNfseDTO userDto = new UserNfseDTO();
        userDto.setLogin(requerente.getCpfCnpj().getCpf());
        userDto.setPassword(requerente.getSenha());
        loteDto.setUserNfseDTO(userDto);
        loteDto.setHomologacao(requerente.isHomologa());
        loteDto.setDataRecebimento(new Date());
        loteDto.setVersaoAbrasf(ABRASF_VERSAO_1_2);

        loteDto.setNumero(loteRps.getNumeroLote().intValue());
        for (TcDeclaracaoPrestacaoServico declaracao : loteRps.getListaRps().getDeclaracaoPrestacaoServico()) {
            RpsNfseDTO dto = new RpsNfseDTO();
            dto.setNumero(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getNumero().toString());
            dto.setSerie(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getSerie());
            dto.setTipoRps(TipoRpsNfseDTO.findByCodigo(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getTipo()));
            dto.setDataEmissao(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getDataEmissao().toGregorianCalendar().getTime());
            dto.setIssCalculado(declaracao.getInfDeclaracaoPrestacaoServico().getServico().getValores().getValorIss());
            dto.setIssPagar(declaracao.getInfDeclaracaoPrestacaoServico().getServico().getValores().getValorIss());
            dto.setCompetencia(declaracao.getInfDeclaracaoPrestacaoServico().getCompetencia().toGregorianCalendar().getTime());

            br.com.webpublico.domain.dto.nfse12.TcDadosServico servico = declaracao.getInfDeclaracaoPrestacaoServico().getServico();

            BigDecimal valorPis = servico.getValores().getValorPis() != null ? servico.getValores().getValorPis() : BigDecimal.ZERO;
            BigDecimal valorCofins = servico.getValores().getValorCofins() != null ? servico.getValores().getValorCofins() : BigDecimal.ZERO;
            BigDecimal valorCsll = servico.getValores().getValorCsll() != null ? servico.getValores().getValorCsll() : BigDecimal.ZERO;
            BigDecimal valorIr = servico.getValores().getValorIr() != null ? servico.getValores().getValorIr() : BigDecimal.ZERO;
            BigDecimal valorInss = servico.getValores().getValorInss() != null ? servico.getValores().getValorInss() : BigDecimal.ZERO;
            BigDecimal outrasRetencoes = servico.getValores().getOutrasRetencoes() != null ? servico.getValores().getOutrasRetencoes() : BigDecimal.ZERO;
            TributosFederaisNfseDTO tributosFederaisDto = new TributosFederaisNfseDTO();

            tributosFederaisDto.setPis(valorPis);
            tributosFederaisDto.setCofins(valorCofins);
            tributosFederaisDto.setIrrf(valorIr);
            tributosFederaisDto.setCsll(valorCsll);
            tributosFederaisDto.setInss(valorInss);
            tributosFederaisDto.setOutrasRetencoes(outrasRetencoes);
            dto.setTributosFederais(tributosFederaisDto);

            dto.setTotalServicos(servico.getValores().getValorServicos());
            dto.setDeducoesLegais(servico.getValores().getValorDeducoes());
            dto.setDescontosIncondicionais(servico.getValores().getDescontoIncondicionado());
            dto.setDescontosCondicionais(servico.getValores().getDescontoCondicionado());
            dto.setTotalNota(dto.getTotalServicos().subtract(dto.getDescontos()));
            dto.setRetencoesFederais(valorPis.add(valorCofins).add(valorCsll)
                    .add(valorIr).add(valorInss).add(outrasRetencoes));
            dto.setValorLiquido(dto.getTotalNota().subtract(dto.getRetencoesFederais()));

            dto.setIssRetido(servico.getIssRetido() == tcSim);
            if (servico.getResponsavelRetencao() != null) {
                dto.setResponsavelRetencao(servico.getResponsavelRetencao() == 1 ? ResponsavelRetencaoNfseDTO.TOMADADOR : ResponsavelRetencaoNfseDTO.INTERMEDIARIO);
            }

            dto.setDescriminacaoServico(servico.getDiscriminacao());
            dto.setNaturezaOperacao(ExigibilidadeNfseDTO.getByCodigo(new Integer(servico.getExigibilidadeISS())));

            if (servico.getValores().getAliquota() != null
                    && servico.getValores().getAliquota().compareTo(new BigDecimal("0.1")) < 0) {
                servico.getValores().setAliquota(servico.getValores().getAliquota().multiply(new BigDecimal("100")));
            }
            for (TcItemServico itemServico : servico.getListaItensServico().getItemServico()) {
                ItemDeclaracaoServicoNfseDTO item = null;
                for (ItemDeclaracaoServicoNfseDTO itemRegistrado : dto.getItens()) {
                    if (itemRegistrado.getServico().getCodigo().equals(itemServico.getItemListaServico())) {
                        item = itemRegistrado;
                        break;
                    }
                }
                if (item == null) {
                    item = new ItemDeclaracaoServicoNfseDTO();
                    CnaeNfseDTO cnaeDto = new CnaeNfseDTO();
                    cnaeDto.setCodigo(itemServico.getCodigoCnae() + "");

                    ServicoNfseDTO servicoDto = new ServicoNfseDTO();
                    servicoDto.setCodigo(itemServico.getItemListaServico());

                    MunicipioNfseDTO municipioDto = new MunicipioNfseDTO();
                    municipioDto.setCodigoIbge(servico.getCodigoMunicipio() + "");

                    PaisNfseDTO paisDto = new PaisNfseDTO();
                    paisDto.setCodigo(servico.getCodigoPais());

                    item.setPrestadoNoPais(Strings.isNullOrEmpty(servico.getCodigoPais()) || ConstantesDomain.CODIGO_BRASIL.equals(servico.getCodigoPais()));
                    item.setServico(servicoDto);
                    item.setCnae(cnaeDto);
                    item.setMunicipio(municipioDto);
                    item.setPais(paisDto);

                    if (servico.getListaItensServico().getItemServico().size() > 1) {
                        item.setNomeServico(servicoDto.getDescricao());
                        item.setDescricao(servicoDto.getDescricao());
                    } else {
                        item.setNomeServico(itemServico.getDescricao());
                        item.setDescricao(itemServico.getDescricao());
                    }
                    item.setAliquotaServico(servico.getValores().getAliquota());
                    //desconto incondicionado, não é enviado no item, somente na nota, então é atribuido no primeiro item
                    item.setDescontosIncondicionados(servico.getValores().getDescontoIncondicionado());
                    //dedução legal, não é enviado no item, somente na nota, então é atribuido no primeiro item
                    item.setDeducoes(servico.getValores().getValorDeducoes());
                    dto.getItens().add(item);
                }
                item.setQuantidade(BigDecimal.ONE);
                item.setValorServico(item.getValorServico().add(itemServico.getQuantidade().multiply(itemServico.getValorUnitario())));
                if (itemServico.getValorDesconto() != null) {
                    item.setDescontosCondicionados(item.getDescontosCondicionados().add((itemServico.getValorDesconto())));
                }
                if (itemServico.getDadosDeducao() != null && itemServico.getDadosDeducao().getValorADeduzir() != null) {
                    item.setDeducoes(item.getDeducoes().add(itemServico.getDadosDeducao().getValorADeduzir()));
                }
                BigDecimal baseCalculo = itemServico.getQuantidade().multiply(itemServico.getValorUnitario());
                baseCalculo = baseCalculo.subtract(itemServico.getValorDesconto());
                if (itemServico.getDadosDeducao() != null)
                    baseCalculo = baseCalculo.subtract(itemServico.getDadosDeducao().getValorADeduzir());
                item.setBaseCalculo(item.getBaseCalculo().add(baseCalculo));

                if (servico.getListaItensServico().getItemServico().size() > 1) {
                    DetalheItemDeclaracaoServicoNfseDTO detalhe = new DetalheItemDeclaracaoServicoNfseDTO();
                    detalhe.setDescricao(itemServico.getDescricao());
                    detalhe.setQuantidade(itemServico.getQuantidade());
                    detalhe.setValorServico(itemServico.getValorUnitario());
                    item.getDetalhes().add(detalhe);
                }
            }

            PrestadorServicoNfseDTO prestadorDto = new PrestadorServicoNfseDTO();
            prestadorDto.setInscricaoMunicipal(getInscricaoMunicipal(declaracao.getInfDeclaracaoPrestacaoServico().getDadosPrestador().getIdentificacaoPrestador()));
            dto.setPrestador(prestadorDto);

            dto.setDadosPessoaisPrestador(new DadosPessoaisNfseDTO());
            if (declaracao.getInfDeclaracaoPrestacaoServico().getDadosPrestador().getIdentificacaoPrestador().getCpfCnpj().getCnpj() != null) {
                dto.getDadosPessoaisPrestador().setCpfCnpj(declaracao.getInfDeclaracaoPrestacaoServico().getDadosPrestador().getIdentificacaoPrestador().getCpfCnpj().getCnpj());
            } else {
                dto.getDadosPessoaisPrestador().setCpfCnpj(declaracao.getInfDeclaracaoPrestacaoServico().getDadosPrestador().getIdentificacaoPrestador().getCpfCnpj().getCpf());
            }

            if (declaracao.getInfDeclaracaoPrestacaoServico().getTomador() != null) {
                br.com.webpublico.domain.dto.nfse12.TcDadosTomador tomador = declaracao.getInfDeclaracaoPrestacaoServico().getTomador();
                DadosPessoaisNfseDTO dadosTomadorDto = new DadosPessoaisNfseDTO();
                if (tomador != null && tomador.getIdentificacaoTomador() != null) {
                    if (tomador.getIdentificacaoTomador().getCpfCnpj().getCnpj() != null) {
                        dadosTomadorDto.setCpfCnpj(tomador.getIdentificacaoTomador().getCpfCnpj().getCnpj());
                    } else {
                        dadosTomadorDto.setCpfCnpj(tomador.getIdentificacaoTomador().getCpfCnpj().getCpf());
                    }
                    dadosTomadorDto.setInscricaoMunicipal(getInscricaoMunicipal(tomador.getIdentificacaoTomador()));
                    dadosTomadorDto.setNomeRazaoSocial(tomador.getRazaoSocial());
                    if (tomador.getEndereco() != null) {
                        dadosTomadorDto.setLogradouro(tomador.getEndereco().getEndereco());
                        dadosTomadorDto.setNumero(tomador.getEndereco().getNumero());
                        dadosTomadorDto.setComplemento(tomador.getEndereco().getComplemento());
                        dadosTomadorDto.setBairro(tomador.getEndereco().getBairro());
                        if (tomador.getEndereco().getCodigoMunicipio() != null) {
                            MunicipioNfseDTO municipioNfseDTO = new MunicipioNfseDTO();
                            municipioNfseDTO.setCodigoIbge(tomador.getEndereco().getCodigoMunicipio() + "");
                            dadosTomadorDto.setMunicipio(municipioNfseDTO);
                        }
                        if (tomador.getEndereco().getCodigoPais() != null) {
                            PaisNfseDTO paisNfseDTO = new PaisNfseDTO();
                            paisNfseDTO.setCodigo(tomador.getEndereco().getCodigoPais() + "");
                            dadosTomadorDto.setPais(paisNfseDTO);
                        }
                        dadosTomadorDto.setBairro(tomador.getEndereco().getBairro());
                        dadosTomadorDto.setCep(tomador.getEndereco().getCep());
                    }
                    if (tomador.getContato() != null) {
                        dadosTomadorDto.setTelefone(tomador.getContato().getTelefone());
                        dadosTomadorDto.setEmail(tomador.getContato().getEmail());
                    }
                }
                dto.setDadosPessoaisTomador(dadosTomadorDto);
            }

            if (declaracao.getInfDeclaracaoPrestacaoServico().getIntermediario() != null) {
                dto.setIntermediario(new IntermediarioServicoNfseDTO());
                dto.getIntermediario().setCpfCnpj(declaracao.getInfDeclaracaoPrestacaoServico().getIntermediario().getIdentificacaoIntermediario().getCpfCnpj().toString());
            }

            if (declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil() != null) {
                ConstrucaoCivilNfseDTO construcaoCivil = new ConstrucaoCivilNfseDTO();
                construcaoCivil.setArt(declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getArt());
                construcaoCivil.setCodigoObra(declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getCodigoObra());
                construcaoCivil.setIncorporacao(declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getIncorporacao() == 1);
                dto.setConstrucaoCivil(construcaoCivil);
            }

            loteDto.getListaRps().add(dto);
        }
        return loteDto;
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse203.TcIdentificacaoRequerente identificacaoRequerente) {
        if (identificacaoRequerente == null) return null;
        return identificacaoRequerente.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(identificacaoRequerente.getCpfCnpj().getCnpj()) ?
                identificacaoRequerente.getCpfCnpj().getCnpj() :
                identificacaoRequerente.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse203.TcIdentificacaoTomador identificacaoTomador) {
        if (identificacaoTomador == null || Strings.isNullOrEmpty(identificacaoTomador.getInscricaoMunicipal())) return null;
        return identificacaoTomador.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(identificacaoTomador.getCpfCnpj().getCnpj()) ?
                identificacaoTomador.getCpfCnpj().getCnpj() :
                identificacaoTomador.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse203.TcIdentificacaoPrestador identificacaoPrestador) {
        if (identificacaoPrestador == null) return null;
        return identificacaoPrestador.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(identificacaoPrestador.getCpfCnpj().getCnpj()) ?
                identificacaoPrestador.getCpfCnpj().getCnpj() :
                identificacaoPrestador.getInscricaoMunicipal();
    }

    public static String getInscricaoMunicipal(br.com.webpublico.domain.dto.nfse203.TcLoteRps loteRps) {
        if (loteRps == null) return null;
        return loteRps.getCpfCnpj() != null &&
                !Strings.isNullOrEmpty(loteRps.getCpfCnpj().getCnpj()) ?
                loteRps.getCpfCnpj().getCnpj() :
                loteRps.getInscricaoMunicipal();
    }

    public static LoteRpsNfseDTO criarLoteRps203(br.com.webpublico.domain.dto.nfse203.TcIdentificacaoRequerente requerente,
                                                 br.com.webpublico.domain.dto.nfse203.TcLoteRps loteRps) throws ValidacaoNfseWSException {
        if (loteRps.getQuantidadeRps() != loteRps.getListaRps().getRps().size()) {
            throw new ValidacaoNfseWSException(TipoValidacao.E069);
        }
        if (loteRps.getNumeroLote() == null) {
            throw new ValidacaoNfseWSException(TipoValidacao.E011);
        }

        LoteRpsNfseDTO loteDto = new LoteRpsNfseDTO();
        UserNfseDTO userDto = new UserNfseDTO();
        userDto.setLogin(requerente.getCpfCnpj().getCpf());
        userDto.setPassword(requerente.getSenha());
        loteDto.setUserNfseDTO(userDto);
        loteDto.setHomologacao(requerente.isHomologa());
        loteDto.setDataRecebimento(new Date());
        loteDto.setNumero(loteRps.getNumeroLote().intValue());
        loteDto.setVersaoAbrasf(ABRASF_VERSAO_2_03);
        for (br.com.webpublico.domain.dto.nfse203.TcDeclaracaoPrestacaoServico declaracao : loteRps.getListaRps().getRps()) {
            if (declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getNumero() == null) {
                throw new ValidacaoNfseWSException(TipoValidacao.E011);
            }
            if (declaracao.getInfDeclaracaoPrestacaoServico().getRps().getDataEmissao() == null) {
                throw new ValidacaoNfseWSException(TipoValidacao.E014);
            }
            RpsNfseDTO dto = new RpsNfseDTO();
            dto.setNumero(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getNumero().toString());
            dto.setSerie(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getSerie());
            dto.setTipoRps(TipoRpsNfseDTO.findByCodigo(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getIdentificacaoRps().getTipo()));
            dto.setDataEmissao(declaracao.getInfDeclaracaoPrestacaoServico().getRps().getDataEmissao().toGregorianCalendar().getTime());
            dto.setCompetencia(declaracao.getInfDeclaracaoPrestacaoServico().getCompetencia().toGregorianCalendar().getTime());
            dto.setIncentivoFiscal(declaracao.getInfDeclaracaoPrestacaoServico().getIncentivoFiscal() == 1);

            br.com.webpublico.domain.dto.nfse203.TcDadosServico servico = declaracao.getInfDeclaracaoPrestacaoServico().getServico();

            TributosFederaisNfseDTO tributosFederaisDto = new TributosFederaisNfseDTO();
            BigDecimal valorPis = servico.getValores().getValorPis() != null ? servico.getValores().getValorPis() : BigDecimal.ZERO;
            BigDecimal valorCofins = servico.getValores().getValorCofins() != null ? servico.getValores().getValorCofins() : BigDecimal.ZERO;
            BigDecimal valorCsll = servico.getValores().getValorCsll() != null ? servico.getValores().getValorCsll() : BigDecimal.ZERO;
            BigDecimal valorIr = servico.getValores().getValorIr() != null ? servico.getValores().getValorIr() : BigDecimal.ZERO;
            BigDecimal valorInss = servico.getValores().getValorInss() != null ? servico.getValores().getValorInss() : BigDecimal.ZERO;
            BigDecimal outrasRetencoes = servico.getValores().getOutrasRetencoes() != null ? servico.getValores().getOutrasRetencoes() : BigDecimal.ZERO;

            tributosFederaisDto.setPis(valorPis);
            tributosFederaisDto.setCofins(valorCofins);
            tributosFederaisDto.setIrrf(valorIr);
            tributosFederaisDto.setCsll(valorCsll);
            tributosFederaisDto.setInss(valorInss);
            tributosFederaisDto.setOutrasRetencoes(outrasRetencoes);
            dto.setTributosFederais(tributosFederaisDto);

            dto.setTotalServicos(servico.getValores().getValorServicos());
            dto.setDeducoesLegais(servico.getValores().getValorDeducoes());
            dto.setDescontosCondicionais(servico.getValores().getDescontoCondicionado());
            dto.setDescontosIncondicionais(servico.getValores().getDescontoIncondicionado());
            dto.setTotalNota(dto.getTotalServicos().subtract(dto.getDescontos()));
            dto.setRetencoesFederais(valorPis.add(valorCofins).add(valorCsll)
                    .add(valorIr).add(valorInss).add(outrasRetencoes));
            dto.setBaseCalculo(dto.getTotalServicos().subtract(dto.getDescontosIncondicionais()).subtract(dto.getDeducoesLegais()));
            dto.setValorLiquido(dto.getTotalNota().subtract(dto.getRetencoesFederais()));
            dto.setIssRetido(servico.getIssRetido() == 1);
            if (servico.getResponsavelRetencao() != null) {
                dto.setResponsavelRetencao(servico.getResponsavelRetencao() == 1 ? ResponsavelRetencaoNfseDTO.TOMADADOR : ResponsavelRetencaoNfseDTO.INTERMEDIARIO);
            }
            dto.setDescriminacaoServico(servico.getDiscriminacao());
            dto.setNaturezaOperacao(ExigibilidadeNfseDTO.getByCodigo(new Integer(servico.getExigibilidadeISS())));

            if (servico.getValores().getAliquota() != null
                    && servico.getValores().getAliquota().compareTo(new BigDecimal("0.1")) < 0) {
                servico.getValores().setAliquota(servico.getValores().getAliquota().multiply(new BigDecimal("100")));
            }

            if (servico.getListaItensServico() == null || servico.getListaItensServico().getItemServico() == null || servico.getListaItensServico().getItemServico().isEmpty()) {
                throw new ValidacaoNfseWSException(TipoValidacao.E030);
            }

            for (br.com.webpublico.domain.dto.nfse203.TcItemServico itemServico : servico.getListaItensServico().getItemServico()) {
                ItemDeclaracaoServicoNfseDTO item = null;
                for (ItemDeclaracaoServicoNfseDTO itemRegistrado : dto.getItens()) {
                    if (itemRegistrado.getServico().getCodigo().equals(itemServico.getItemListaServico())) {
                        item = itemRegistrado;
                        break;
                    }
                }
                if (item == null) {
                    item = new ItemDeclaracaoServicoNfseDTO();
                    CnaeNfseDTO cnaeDto = new CnaeNfseDTO();
                    cnaeDto.setCodigo(itemServico.getCodigoCnae() + "");

                    ServicoNfseDTO servicoDto = new ServicoNfseDTO();
                    servicoDto.setCodigo(itemServico.getItemListaServico());

                    MunicipioNfseDTO municipioDto = new MunicipioNfseDTO();
                    municipioDto.setCodigoIbge(servico.getCodigoMunicipio() + "");

                    PaisNfseDTO paisDto = new PaisNfseDTO();
                    paisDto.setCodigo(servico.getCodigoPais());

                    item.setPrestadoNoPais(Strings.isNullOrEmpty(servico.getCodigoPais()) || ConstantesDomain.CODIGO_BRASIL.equals(servico.getCodigoPais()));
                    item.setServico(servicoDto);
                    item.setCnae(cnaeDto);
                    item.setMunicipio(municipioDto);
                    item.setPais(paisDto);
                    if (servico.getListaItensServico().getItemServico().size() > 1) {
                        item.setNomeServico(servicoDto.getDescricao());
                        item.setDescricao(servicoDto.getDescricao());
                    } else {
                        item.setNomeServico(itemServico.getDescricao());
                        item.setDescricao(itemServico.getDescricao());
                    }
                    item.setAliquotaServico(servico.getValores().getAliquota());
                    //desconto incondicionado, não é enviado no item, somente na nota, então é atribuido no primeiro item
                    item.setDescontosIncondicionados(servico.getValores().getDescontoIncondicionado());
                    //dedulão legal, não é enviado no item, somente na nota, então é atribuido no primeiro item
                    item.setDeducoes(servico.getValores().getValorDeducoes());
                    dto.getItens().add(item);
                }
                item.setQuantidade(BigDecimal.ONE);
                if (itemServico.getQuantidade() != null && itemServico.getValorUnitario() != null) {
                    item.setValorServico(item.getValorServico().add(itemServico.getQuantidade().multiply(itemServico.getValorUnitario())));
                }
                if (itemServico.getValorDesconto() != null) {
                    item.setDescontosCondicionados(item.getDescontosCondicionados().add((itemServico.getValorDesconto())));
                }
                if (itemServico.getDadosDeducao() != null && itemServico.getDadosDeducao().getValorADeduzir() != null) {
                    item.setDeducoes(item.getDeducoes().add(itemServico.getDadosDeducao().getValorADeduzir()));
                }
                BigDecimal baseCalculo = BigDecimal.ZERO;
                if (itemServico.getQuantidade() != null && itemServico.getValorUnitario() != null) {
                    baseCalculo = itemServico.getQuantidade().multiply(itemServico.getValorUnitario());
                }
                baseCalculo = baseCalculo.subtract(item.getDescontosCondicionados());
                baseCalculo = baseCalculo.subtract(item.getDeducoes());
                item.setBaseCalculo(item.getBaseCalculo().add(baseCalculo));
                if (servico.getListaItensServico().getItemServico().size() > 1) {
                    DetalheItemDeclaracaoServicoNfseDTO detalhe = new DetalheItemDeclaracaoServicoNfseDTO();
                    detalhe.setDescricao(itemServico.getDescricao());
                    detalhe.setQuantidade(itemServico.getQuantidade());
                    detalhe.setValorServico(itemServico.getValorUnitario());
                    item.getDetalhes().add(detalhe);
                }
            }

            if (declaracao.getInfDeclaracaoPrestacaoServico().getPrestador() != null) {
                PrestadorServicoNfseDTO prestadorDto = new PrestadorServicoNfseDTO();
                prestadorDto.setInscricaoMunicipal(getInscricaoMunicipal(declaracao.getInfDeclaracaoPrestacaoServico().getPrestador()));
                dto.setPrestador(prestadorDto);
                dto.setDadosPessoaisPrestador(new DadosPessoaisNfseDTO());
                if (declaracao.getInfDeclaracaoPrestacaoServico().getPrestador().getCpfCnpj().getCnpj() != null) {
                    dto.getDadosPessoaisPrestador().setCpfCnpj(declaracao.getInfDeclaracaoPrestacaoServico().getPrestador().getCpfCnpj().getCnpj());
                } else {
                    dto.getDadosPessoaisPrestador().setCpfCnpj(declaracao.getInfDeclaracaoPrestacaoServico().getPrestador().getCpfCnpj().getCpf());
                }
            } else if (loteRps.getInscricaoMunicipal() != null) {
                PrestadorServicoNfseDTO prestadorDto = new PrestadorServicoNfseDTO();
                prestadorDto.setInscricaoMunicipal(getInscricaoMunicipal(loteRps));
                dto.setPrestador(prestadorDto);
                dto.setDadosPessoaisPrestador(new DadosPessoaisNfseDTO());
                if (loteRps.getCpfCnpj().getCnpj() != null) {
                    dto.getDadosPessoaisPrestador().setCpfCnpj(loteRps.getCpfCnpj().getCnpj());
                } else {
                    dto.getDadosPessoaisPrestador().setCpfCnpj(loteRps.getCpfCnpj().getCpf());
                }
            }

            if (declaracao.getInfDeclaracaoPrestacaoServico().getTomador() != null) {
                br.com.webpublico.domain.dto.nfse203.TcDadosTomador tcDadosTomador = declaracao.getInfDeclaracaoPrestacaoServico().getTomador();
                DadosPessoaisNfseDTO dadosTomadorDto = new DadosPessoaisNfseDTO();
                if (tcDadosTomador.getIdentificacaoTomador() != null) {
                    dadosTomadorDto.setInscricaoMunicipal(getInscricaoMunicipal(tcDadosTomador.getIdentificacaoTomador()));
                    if (tcDadosTomador.getIdentificacaoTomador().getCpfCnpj() != null) {
                        if (tcDadosTomador.getIdentificacaoTomador().getCpfCnpj().getCnpj() != null) {
                            dadosTomadorDto.setCpfCnpj(tcDadosTomador.getIdentificacaoTomador().getCpfCnpj().getCnpj());
                        } else {
                            dadosTomadorDto.setCpfCnpj(tcDadosTomador.getIdentificacaoTomador().getCpfCnpj().getCpf());
                        }
                    }
                }

                dadosTomadorDto.setNomeRazaoSocial(tcDadosTomador.getRazaoSocial());
                if (tcDadosTomador.getEndereco() != null) {
                    dadosTomadorDto.setLogradouro(tcDadosTomador.getEndereco().getEndereco());
                    dadosTomadorDto.setNumero(tcDadosTomador.getEndereco().getNumero());
                    dadosTomadorDto.setComplemento(tcDadosTomador.getEndereco().getComplemento());
                    dadosTomadorDto.setBairro(tcDadosTomador.getEndereco().getBairro());
                    if (tcDadosTomador.getEndereco().getCodigoMunicipio() != null) {
                        MunicipioNfseDTO municipioNfseDTO = new MunicipioNfseDTO();
                        municipioNfseDTO.setCodigoIbge(tcDadosTomador.getEndereco().getCodigoMunicipio() + "");
                        dadosTomadorDto.setMunicipio(municipioNfseDTO);
                    }
                    if (tcDadosTomador.getEndereco().getCodigoPais() != null) {
                        PaisNfseDTO paisNfseDTO = new PaisNfseDTO();
                        paisNfseDTO.setCodigo(tcDadosTomador.getEndereco().getCodigoPais() + "");
                        dadosTomadorDto.setPais(paisNfseDTO);
                    }
                    dadosTomadorDto.setBairro(tcDadosTomador.getEndereco().getBairro());
                    dadosTomadorDto.setCep(tcDadosTomador.getEndereco().getCep());
                }
                if (tcDadosTomador.getContato() != null) {
                    dadosTomadorDto.setTelefone(tcDadosTomador.getContato().getTelefone());
                    dadosTomadorDto.setEmail(tcDadosTomador.getContato().getEmail());
                }
                dto.setDadosPessoaisTomador(dadosTomadorDto);
            }
            dto.setIntermediario(new IntermediarioServicoNfseDTO());
            if (declaracao.getInfDeclaracaoPrestacaoServico().getIntermediario() != null)
                dto.getIntermediario().setCpfCnpj(declaracao.getInfDeclaracaoPrestacaoServico().getIntermediario().getIdentificacaoIntermediario().getCpfCnpj().toString());

            if (declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil() != null) {
                ConstrucaoCivilNfseDTO construcaoCivil = new ConstrucaoCivilNfseDTO();
                construcaoCivil.setArt(declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getArt());
                construcaoCivil.setCodigoObra(declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getCodigoObra());
                if (declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getIncorporacao() != null)
                    construcaoCivil.setIncorporacao(declaracao.getInfDeclaracaoPrestacaoServico().getConstrucaoCivil().getIncorporacao() == 1);
                dto.setConstrucaoCivil(construcaoCivil);
            }

            loteDto.getListaRps().add(dto);
        }
        return loteDto;
    }

    public static EnviarLoteRpsEnvio getEnviarLoteRpsEnvio10(String xml) {
        xml = handleXml(xml);
        return (EnviarLoteRpsEnvio) Util.xmlToObject(xml, EnviarLoteRpsEnvio.class);
    }

    private static LoteRpsNfseDTO getLoteRps10AssincronoFromXml(String xml) {
        try {
            EnviarLoteRpsEnvio enviarLoteRpsEnvio = getEnviarLoteRpsEnvio10(xml);
            return br.com.webpublico.domain.dto.util.LoteRpsBuild.criarLoteRps10(enviarLoteRpsEnvio.getLoteRps());
        } catch (Exception | ValidacaoNfseWSException e) {
            return null;
        }
    }

    private static List<RpsNfseDTO> getListaRps10AssincronoFromXml(String xml) {
        try {
            return getLoteRps10AssincronoFromXml(xml).getListaRps();
        } catch (Exception e) {
            return null;
        }
    }


    public static br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsEnvio getEnviarLoteRpsEnvio12(String xml) {
        xml = handleXml(xml);
        return (br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsEnvio) Util.xmlToObject(xml, br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsEnvio.class);
    }

    private static LoteRpsNfseDTO getLoteRps12AssincronoFromXml(String xml) {
        try {
            br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsEnvio enviarLoteRpsEnvio = getEnviarLoteRpsEnvio12(xml);
            return br.com.webpublico.domain.dto.util.LoteRpsBuild.criarLoteRps12(enviarLoteRpsEnvio.getIdentificacaoRequerente(), enviarLoteRpsEnvio.getLoteRps());
        } catch (Exception | ValidacaoNfseWSException e) {
            return null;
        }
    }

    private static List<RpsNfseDTO> getListaRps12AssincronoFromXml(String xml) {
        try {
            return getLoteRps12AssincronoFromXml(xml).getListaRps();
        } catch (Exception e) {
            return null;
        }
    }

    public static br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsSincronoEnvio getEnviarLoteRpsSincronoEnvio12(String xml) {
        xml = handleXml(xml);
        return (br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsSincronoEnvio) Util.xmlToObject(xml, br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsSincronoEnvio.class);
    }

    private static LoteRpsNfseDTO getLoteRps12SincronoFromXml(String xml) {
        try {
            br.com.webpublico.domain.dto.nfse12.EnviarLoteRpsSincronoEnvio enviarLoteRpsEnvio = getEnviarLoteRpsSincronoEnvio12(xml);
            return br.com.webpublico.domain.dto.util.LoteRpsBuild.criarLoteRps12(enviarLoteRpsEnvio.getIdentificacaoRequerente(), enviarLoteRpsEnvio.getLoteRps());
        } catch (Exception | ValidacaoNfseWSException e) {
            return null;
        }
    }

    private static List<RpsNfseDTO> getListaRps12SincronoFromXml(String xml) {
        try {
            return getLoteRps12SincronoFromXml(xml).getListaRps();
        } catch (Exception e) {
            return null;
        }
    }

    public static br.com.webpublico.domain.dto.nfse203.EnviarLoteRpsEnvio getEnviarLoteRpsEnvio203(String xml) {
        xml = handleXml(xml);
        return (br.com.webpublico.domain.dto.nfse203.EnviarLoteRpsEnvio) Util.xmlToObject(xml, br.com.webpublico.domain.dto.nfse203.EnviarLoteRpsEnvio.class);
    }

    private static LoteRpsNfseDTO getLoteRps203AssincronoFromXml(String xml) {
        try {
            br.com.webpublico.domain.dto.nfse203.EnviarLoteRpsEnvio enviarLoteRpsEnvio = getEnviarLoteRpsEnvio203(xml);
            return br.com.webpublico.domain.dto.util.LoteRpsBuild.criarLoteRps203(enviarLoteRpsEnvio.getIdentificacaoRequerente(), enviarLoteRpsEnvio.getLoteRps());
        } catch (Exception | ValidacaoNfseWSException e) {
            return null;
        }
    }

    private static List<RpsNfseDTO> getListaRps203AssincronoFromXml(String xml) {
        try {
            return getLoteRps203AssincronoFromXml(xml).getListaRps();
        } catch (Exception e) {
            return null;
        }
    }

    public static EnviarLoteRpsSincronoEnvio getEnviarLoteRpsSincronoEnvio203(String xml) {
        xml = handleXml(xml);
        return (EnviarLoteRpsSincronoEnvio) Util.xmlToObject(xml, EnviarLoteRpsSincronoEnvio.class);
    }

    private static LoteRpsNfseDTO getLoteRps203SincronoFromXml(String xml) {
        try {
            EnviarLoteRpsSincronoEnvio enviarLoteRpsEnvio = getEnviarLoteRpsSincronoEnvio203(xml);
            return br.com.webpublico.domain.dto.util.LoteRpsBuild.criarLoteRps203(enviarLoteRpsEnvio.getIdentificacaoRequerente(), enviarLoteRpsEnvio.getLoteRps());
        } catch (Exception | ValidacaoNfseWSException e) {
            return null;
        }
    }

    private static List<RpsNfseDTO> getListaRps203SincronoFromXml(String xml) {
        try {
            return getLoteRps203SincronoFromXml(xml).getListaRps();
        } catch (Exception e) {
            return null;
        }
    }

    public static LoteRpsNfseDTO criarLoteRps(String xml) throws ValidacaoNfseWSException {
        try {
            LoteRpsNfseDTO lote = null;
            if (xml.indexOf("<EnviarLoteRpsEnvio") >= 0) {
                String entrada = xml.substring(xml.indexOf("<EnviarLoteRpsEnvio"), xml.lastIndexOf("</EnviarLoteRpsEnvio>") + "</EnviarLoteRpsEnvio>".length());

                lote = getLoteRps10AssincronoFromXml(entrada);
                if (lote == null) {
                    lote = getLoteRps12AssincronoFromXml(entrada);
                    if (lote == null) {
                        lote = getLoteRps203AssincronoFromXml(entrada);
                    }
                }
            } else if (xml.indexOf("<EnviarLoteRpsSincronoEnvio") >= 0) {
                String entrada = xml.substring(xml.indexOf("<EnviarLoteRpsSincronoEnvio"), xml.lastIndexOf("</EnviarLoteRpsSincronoEnvio>") + "</EnviarLoteRpsSincronoEnvio>".length());

                lote = getLoteRps12SincronoFromXml(entrada);
                if (lote == null) {
                    lote = getLoteRps203SincronoFromXml(entrada);
                }
            }

            if (lote == null) {
                throw new Exception("Erro de converso");
            } else {
                return lote;
            }
        } catch (Exception e) {
            throw new ValidacaoNfseWSException(TipoValidacao.W004);
        }
    }

    public static List<RpsNfseDTO> getListaRpsFromXml(String xml) throws ValidacaoNfseWSException {
        List<RpsNfseDTO> listaRps = null;

        try {
            if (xml.indexOf("<EnviarLoteRpsEnvio") >= 0) {
                String entrada = xml.substring(xml.indexOf("<EnviarLoteRpsEnvio"), xml.lastIndexOf("</EnviarLoteRpsEnvio>") + "</EnviarLoteRpsEnvio>".length());

                listaRps = getListaRps10AssincronoFromXml(entrada);
                if (listaRps == null) {
                    listaRps = getListaRps12AssincronoFromXml(entrada);
                    if (listaRps == null) {
                        listaRps = getListaRps203AssincronoFromXml(entrada);
                    }
                }
            } else if (xml.indexOf("<EnviarLoteRpsSincronoEnvio") >= 0) {
                String entrada = xml.substring(xml.indexOf("<EnviarLoteRpsSincronoEnvio"), xml.lastIndexOf("</EnviarLoteRpsSincronoEnvio>") + "</EnviarLoteRpsSincronoEnvio>".length());

                listaRps = getListaRps12SincronoFromXml(entrada);
                if (listaRps == null) {
                    listaRps = getListaRps203SincronoFromXml(entrada);
                }
            }

            if (listaRps == null) {
                throw new Exception("Erro de converso");
            } else {
                return listaRps;
            }
        } catch (Exception e) {
            throw new ValidacaoNfseWSException(TipoValidacao.W004);
        }
    }

    private static String handleXml(String xml) {
        return xml.replace("xmlns=\"http://www.e-nfs.com.br\"", "")
                .replace("xmlns=\"http://www.e-nfs.com.br\"", "")
                .replace("xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"", "")
                .replace("xmlns=\"http://nfse.webpublico.com.br/iss/nfse_v1\"", "")
                .replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "")
                .replace("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }

}
