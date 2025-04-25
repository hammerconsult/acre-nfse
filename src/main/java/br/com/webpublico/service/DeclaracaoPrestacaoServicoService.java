package br.com.webpublico.service;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.repository.CadastroEconomicoJDBCRepository;
import br.com.webpublico.repository.DadosPessoaisJDBCRepository;
import br.com.webpublico.repository.DeclaracaoPrestacaoServicoJDBCRepository;
import br.com.webpublico.repository.DetalheItemDeclaracaoServicoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class DeclaracaoPrestacaoServicoService implements Serializable {

    @Autowired
    private DeclaracaoPrestacaoServicoJDBCRepository declaracaoPrestacaoServicoJDBCRepository;
    @Autowired
    private TributosFederaisService tributosFederaisService;
    @Autowired
    private TomadorService tomadorService;
    @Autowired
    private DadosPessoaisJDBCRepository dadosPessoaisJDBCRepository;
    @Autowired
    private CadastroEconomicoJDBCRepository cadastroEconomicoJDBCRepository;
    @Autowired
    private IntermediarioServicoService inserirIntermediarioServico;
    @Autowired
    private ItemDeclaracaoPrestacaoServicoService itemDeclaracaoPrestacaoServicoService;
    @Autowired
    private DetalheItemDeclaracaoServicoJDBCRepository detalheItemDeclaracaoServicoJDBCRepository;
    @Autowired
    private ConstrucaoCivilService construcaoCivilService;
    @Autowired
    private CancelamentoService cancelamentoService;
    @Autowired
    private DadosPessoaisService dadosPessoaisService;

    public void inserir(NotaFiscalNfseDTO dto) {
        DeclaracaoPrestacaoServicoNfseDTO dps = dto.getDeclaracaoPrestacaoServico();
        if (dps.getTributosFederais() != null) {
            tributosFederaisService.inserir(dps.getTributosFederais());
        }
        sincronizarDadosPrestador(dto);
        dadosPessoaisJDBCRepository.inserirDadosPessoais(dps.getDadosPessoaisPrestador());
        if (!NotaFiscalNfseDTO.ModalidadeEmissao.NAO_IDENTIFICADO.equals(dto.getModalidade())) {
            if (!dto.getTomadorHabitual()) {
                dto.getTomador().setPrestador(null);
            }
            tomadorService.salvar(dto.getTomador());
        }
        if (dps.getIntermediario() != null) {
            inserirIntermediarioServico.inserirIntermediarioServico(dps.getIntermediario());
        }
        Date hoje = new Date();
        if (dps.getCompetencia() == null || dps.getCompetencia().after(hoje)) {
            dps.setCompetencia(hoje);
        }
        declaracaoPrestacaoServicoJDBCRepository.inserirDeclaracaoPrestacaoServico(dto);
        for (ItemDeclaracaoServicoNfseDTO item : dps.getItens()) {
            if (item.getAliquotaServico() == null || item.getAliquotaServico().compareTo(BigDecimal.ZERO) == 0) {
                item.setAliquotaServico(dto.getAliquotaServico());
            }
            itemDeclaracaoPrestacaoServicoService.inserir(dps, item);
        }
    }

    public void inserir(ServicoDeclaradoNfseDTO dto) {
        DeclaracaoPrestacaoServicoNfseDTO dps = dto.getDeclaracaoPrestacaoServico();
        dadosPessoaisJDBCRepository.inserirDadosPessoais(dps.getDadosPessoaisPrestador());
        if (dps.getDadosPessoaisTomador() != null)
            dadosPessoaisJDBCRepository.inserirDadosPessoais(dps.getDadosPessoaisTomador());
        if (dps.getIntermediario() != null)
            inserirIntermediarioServico.inserirIntermediarioServico(dps.getIntermediario());
        declaracaoPrestacaoServicoJDBCRepository.inserirDeclaracaoPrestacaoServico(dto);
        for (ItemDeclaracaoServicoNfseDTO item : dps.getItens()) {
            itemDeclaracaoPrestacaoServicoService.inserir(dps, item);
        }
    }

    public void inserir(DeclaracaoPrestacaoServicoNfseDTO dto) {
        if (dto.getTributosFederais() != null) {
            tributosFederaisService.inserir(dto.getTributosFederais());
        }
        dadosPessoaisJDBCRepository.inserirDadosPessoais(dto.getDadosPessoaisPrestador());
        if (dto.getDadosPessoaisTomador() != null) {
            dadosPessoaisJDBCRepository.inserirDadosPessoais(dto.getDadosPessoaisTomador());
        }
        if (dto.getIntermediario() != null) {
            inserirIntermediarioServico.inserirIntermediarioServico(dto.getIntermediario());
        }
        declaracaoPrestacaoServicoJDBCRepository.inserirDeclaracaoPrestacaoServico(dto);
        for (ItemDeclaracaoServicoNfseDTO item : dto.getItens()) {
            if (item.getAliquotaServico() == null || item.getAliquotaServico().compareTo(BigDecimal.ZERO) == 0) {
                item.setAliquotaServico(dto.getAliquotaServico());
            }
            itemDeclaracaoPrestacaoServicoService.inserir(dto, item);
        }
    }

    private void sincronizarDadosPrestador(NotaFiscalNfseDTO nota) {
        try {
            PrestadorServicoNfseDTO prestador = cadastroEconomicoJDBCRepository.findById(nota.getPrestador().getId());
            nota.getDeclaracaoPrestacaoServico().setDadosPessoaisPrestador(prestador.getPessoa().getDadosPessoais().clone());
            nota.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().setInscricaoMunicipal(prestador.getInscricaoMunicipal());
            nota.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().setTipoIssqn(prestador.getEnquadramentoFiscal().getTipoIssqn());
            nota.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().setRegimeTributario(prestador.getEnquadramentoFiscal().getRegimeTributario());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DeclaracaoPrestacaoServicoNfseDTO findById(Long id) {
        DeclaracaoPrestacaoServicoNfseDTO dto = declaracaoPrestacaoServicoJDBCRepository.findById(id);
        if (dto.getIntermediario() != null) {
            dto.setIntermediario(inserirIntermediarioServico.findById(dto.getIntermediario().getId()));
        }
        if (dto.getConstrucaoCivil() != null) {
            dto.setConstrucaoCivil(construcaoCivilService.findById(dto.getConstrucaoCivil().getId()));
        }
        if (dto.getTributosFederais() != null) {
            dto.setTributosFederais(tributosFederaisService.findById(dto.getTributosFederais().getId()));
        }
        if (dto.getCancelamento() != null) {
            dto.setCancelamento(cancelamentoService.findById(dto.getCancelamento().getId()));
        }
        dto.setItens(itemDeclaracaoPrestacaoServicoService.findByDeclaracao(dto.getId()));
        if (dto.getDadosPessoaisPrestador() != null) {
            dto.setDadosPessoaisPrestador(dadosPessoaisJDBCRepository.findById(dto.getDadosPessoaisPrestador().getId()));
        }
        if (dto.getDadosPessoaisTomador() != null) {
            dto.setDadosPessoaisTomador(dadosPessoaisJDBCRepository.findById(dto.getDadosPessoaisTomador().getId()));
        }
        dto.setCancelamento(cancelamentoService.findByDeclaracaoIdAndDeferido(dto.getId()));
        return dto;
    }

    public void atribuirCancelamento(DeclaracaoPrestacaoServicoNfseDTO declaracao) {
        declaracaoPrestacaoServicoJDBCRepository.cancelar(declaracao);
    }

    public void delete(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        for (ItemDeclaracaoServicoNfseDTO item : declaracaoPrestacaoServico.getItens()) {
            itemDeclaracaoPrestacaoServicoService.delete(item);
        }
        declaracaoPrestacaoServicoJDBCRepository.delete(declaracaoPrestacaoServico);
    }
}
