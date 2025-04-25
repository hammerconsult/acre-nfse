package br.com.webpublico.service;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.enums.*;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.repository.CancelamentoJDBCRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CancelamentoService implements Serializable {

    @Autowired
    private CancelamentoJDBCRepository cancelamentoJDBCRepository;
    @Autowired
    private DeclaracaoMensalServicoService declaracaoMensalServicoService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private UserService userService;
    @Autowired
    private FeriadoService feriadoService;
    @Autowired
    private NotificacaoService notificacaoService;

    public CancelamentoNfseDTO findById(Long id) {
        return cancelamentoJDBCRepository.findById(id);
    }

    public List<CancelamentoNfseDTO> findByNotaId(Long id) {
        return cancelamentoJDBCRepository.findByNotaId(id);
    }

    public List<CancelamentoNfseDTO> findByDeclaracaoId(Long id) {
        return cancelamentoJDBCRepository.findByDeclaracaoId(id);
    }

    public CancelamentoNfseDTO salvarCancelamento(CancelamentoNfseDTO dto,
                                                  DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico,
                                                  DadosPessoaisNfseDTO dadosPessoaisPrestador,
                                                  SituacaoDeclaracaoNfseDTO situacaoDeclaracao) {
        if (SituacaoDeclaracaoNfseDTO.CANCELADA.equals(situacaoDeclaracao)) {
            return null;
        }

        ConfiguracaoNfseDTO configuracaoNfse = configuracaoService.getConfiguracaoFromServer();

        if (!configuracaoNfse.getPermiteCancelamentoForaPrazo() && passouDaDataLimiteParaCancelamento(configuracaoNfse, dto)) {
            throw new NfseOperacaoNaoPermitidaException(configuracaoNfse.getTextoBloqueioCancelamentoForaPrazo());
        }

        if (!cancelamentoJDBCRepository.findByDeclaracaoIdAndSituacao(declaracaoPrestacaoServico.getId(), SituacaoDeferimentoNfseDTO.EM_ANALISE).isEmpty()) {
            throw new NfseOperacaoNaoPermitidaException("A declaração de serviço já possui uma solicitação de cancelamento em análise.");
        }

        if (configuracaoNfse.getQuantidadeCancelamentosPermitidos() != null) {
            List<CancelamentoNfseDTO> cancelamentosAnteriores = findByDeclaracaoId(declaracaoPrestacaoServico.getId());
            if (cancelamentosAnteriores.size() >= configuracaoNfse.getQuantidadeCancelamentosPermitidos()) {
                throw new NfseOperacaoNaoPermitidaException("A declaração de serviço já possui o número máximo de solicitações de cancelamento.");
            }
        }

        if (declaracaoMensalServicoService.hasDeclaracaoMensalServico(declaracaoPrestacaoServico)) {
            throw new NfseOperacaoNaoPermitidaException("Existe uma declaração mensal de serviços para essa nota fiscal," +
                    " cancele a declaração para cancelar a nota fiscal.");
        }

        dto.setTipoCancelamento(TipoCancelamentoNfseDTO.AUTOMATICO);

        try {
            UserNfseDTO user = userService.getUserWithAuthorities();
            dto.setSolicitante(user.getNome());
        } catch (Exception e) {
            dto.setSolicitante(dadosPessoaisPrestador.getCpfCnpj() + " - " + dadosPessoaisPrestador.getNomeRazaoSocial());
        }

        dto.setSituacaoTomador(SituacaoDeferimentoNfseDTO.DEFERIDO);
        dto.setDataDeferimentoTomador(new Date());
        dto.setSituacaoFiscal(SituacaoDeferimentoNfseDTO.DEFERIDO);
        dto.setDataDeferimentoFiscal(new Date());
        dto.setDataCancelamento(new Date());

        inserir(dto);

        if (!SituacaoDeferimentoNfseDTO.DEFERIDO.equals(dto.getSituacaoFiscal()) ||
                !SituacaoDeferimentoNfseDTO.DEFERIDO.equals(dto.getSituacaoTomador())) {
            notificacaoService.save(new NotificacaoNfseDTO("Nova Solicitação de Cancelamento de Documento Fiscal",
                    "Acesse o link para realizar a análise",
                    "/nfse/cancelamento-nfse/ver/" + dto.getId() + "/",
                    GravidadeNfseDTO.ERRO, TipoNotificacaoNfseDTO.CANCELAMENTO_NFS_ELETRONICA));
        }

        return dto;
    }

    private boolean passouDaDataLimiteParaCancelamento(ConfiguracaoNfseDTO configuracaoNfse, CancelamentoNfseDTO cancelamento) {
        try {
            if (configuracaoNfse.getDiaLimiteCancelamento() == null) {
                return false;
            }
            Calendar limite = Calendar.getInstance();
            limite.setTime(cancelamento.getNotaFiscal().getEmissao());
            limite.setTime(DateUtils.adicionarMeses(limite.getTime(), 1));
            limite.set(Calendar.DAY_OF_MONTH, configuracaoNfse.getDiaLimiteCancelamento());
            limite.setTime(feriadoService.proximoDiaUtil(limite.getTime()));
            return LocalDate.now().isAfter(new LocalDate(limite.getTime()));
        } catch (Exception e) {
            return false;
        }
    }

    public void inserir(CancelamentoNfseDTO dto) {
        cancelamentoJDBCRepository.inserir(dto);
    }

    public List<CancelamentoNfseDTO> findNaoVisualizadosByPrestadorId(Long prestador) {
        return cancelamentoJDBCRepository.findNaoVisualizadosByPrestadorId(prestador);
    }

    public CancelamentoNfseDTO marcarCancelamentoVisualizado(CancelamentoNfseDTO dto) {
        cancelamentoJDBCRepository.marcarCancelamentoVisualizado(dto);
        return dto;
    }

    public Page<CancelamentoNfseDTO> consultarCancelamentos(Pageable pageable,
                                                            List<ParametroQuery> parametros,
                                                            String orderBy) throws Exception {
        List<CancelamentoNfseDTO> dtos = cancelamentoJDBCRepository.findByParam(pageable, parametros,
                orderBy);

        Integer count = cancelamentoJDBCRepository.contarCacnelamentos(parametros);

        return new PageImpl<>(dtos, pageable, count);
    }

    public CancelamentoNfseDTO findByDeclaracaoIdAndDeferido(Long declaracaoId) {
        List<CancelamentoNfseDTO> cancelamentos = cancelamentoJDBCRepository.findByDeclaracaoIdAndSituacao(declaracaoId, SituacaoDeferimentoNfseDTO.DEFERIDO);
        return cancelamentos.stream().findFirst().orElse(null);
    }
}
