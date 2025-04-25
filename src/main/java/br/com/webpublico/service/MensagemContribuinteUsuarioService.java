package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.domain.dto.MensagemContribuinteUsuarioDocumentoNfseDTO;
import br.com.webpublico.domain.dto.MensagemContribuinteUsuarioNfseDTO;
import br.com.webpublico.domain.dto.NotificacaoNfseDTO;
import br.com.webpublico.domain.dto.enums.GravidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMensagemContribuinteNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoNotificacaoNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.MensagemContribuinteUsuarioJDBCRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
public class MensagemContribuinteUsuarioService extends AbstractService<MensagemContribuinteUsuarioNfseDTO> {

    @Autowired
    private MensagemContribuinteUsuarioJDBCRepository repository;
    @Autowired
    private DetentorArquivoComposicaoService detentorArquivoComposicaoService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private NotificacaoService notificacaoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(MensagemContribuinteUsuarioNfseDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getMensagem().getDetentorArquivoComposicao() != null) {
            dto.getMensagem().setDetentorArquivoComposicao(detentorArquivoComposicaoService.findById(dto.getMensagem().getDetentorArquivoComposicao().getId()));
        }
        if (!dto.getLida()) {
            dto.setDocumentos(repository.buscarDocumentosParaUpload(dto));
        } else {
            dto.setDocumentos(repository.buscarDocumentos(dto));
        }
    }

    private void criarNotificacaoResposta(MensagemContribuinteUsuarioNfseDTO dto) {
        if (TipoMensagemContribuinteNfseDTO.QUESTIONAMENTO.equals(dto.getMensagem().getTipo())) {
            notificacaoService.save(new NotificacaoNfseDTO("Resposta de questionamento da nota fiscal eletrônica de serviços",
                    "Acesse o link para visualizar a resposta",
                    "/nfse/mensagem-contribuinte/ver/" + dto.getMensagem().getId() + "/",
                    GravidadeNfseDTO.INFORMACAO, TipoNotificacaoNfseDTO.RESPOSTA_QUESTIONAMENTO_NFSE));
        }
    }

    public void marcarMensagemComoLida(MensagemContribuinteUsuarioNfseDTO mensagemContribuinteUsuario) throws IOException {
        tratarDocumentos(mensagemContribuinteUsuario);
        repository.marcarMensagemComoLida(mensagemContribuinteUsuario);
        criarNotificacaoResposta(mensagemContribuinteUsuario);
    }

    public void tratarDocumentos(MensagemContribuinteUsuarioNfseDTO mensagemContribuinteUsuario) throws IOException {
        if (mensagemContribuinteUsuario.getDocumentos() != null) {
            for (MensagemContribuinteUsuarioDocumentoNfseDTO documento : mensagemContribuinteUsuario.getDocumentos()) {
                if (!Strings.isNullOrEmpty(documento.getFile())) {
                    documento.setArquivo(new ArquivoNfseDTO().toArquivo(documento.getFile(), documento.getFileName()));
                    arquivoService.save(documento.getArquivo());
                }
            }
        }
    }

    public MensagemContribuinteUsuarioNfseDTO buscarProximaMensagemNaoLida(Long prestador) throws Exception {
        Integer prazoAvisoNovaMensagem = configuracaoService.getConfiguracaoFromServer().getPrazoAvisoNovaMensagem();
        MensagemContribuinteUsuarioNfseDTO dto = repository.buscarProximaMensagemNaoLida(prestador, prazoAvisoNovaMensagem);
        preencher(dto);
        return dto;
    }

    public Integer countMensagensNaoLidaNoPrazo(Long prestador) {
        Integer prazoAvisoNovaMensagem = configuracaoService.getConfiguracaoFromServer().getPrazoAvisoNovaMensagem();
        return repository.countMensagensNaoLidaNoPrazo(prestador, prazoAvisoNovaMensagem);
    }
}
