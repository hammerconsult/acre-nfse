package br.com.webpublico.service;

import br.com.webpublico.domain.dto.FaleConoscoNfseDTO;
import br.com.webpublico.domain.dto.NotificacaoNfseDTO;
import br.com.webpublico.domain.dto.enums.GravidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoFaleConoscoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoNotificacaoNfseDTO;
import br.com.webpublico.repository.FaleConoscoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FaleConoscoService extends AbstractWPService<FaleConoscoNfseDTO> {

    @Autowired
    FaleConoscoJDBCRepository faleConoscoJDBCRepository;
    @Autowired
    NotificacaoService notificacaoService;

    public FaleConoscoNfseDTO inserir(FaleConoscoNfseDTO dto) {
        dto.setDataEnvio(new Date());
        dto.setSituacao(SituacaoFaleConoscoNfseDTO.ABERTO);
        dto = faleConoscoJDBCRepository.inserir(dto);
        notificar(dto);
        return dto;
    }

    private void notificar(FaleConoscoNfseDTO dto) {
        NotificacaoNfseDTO notificacao = new NotificacaoNfseDTO();
        notificacao.setDescricao("Existe uma nova mensagem do Fale Conosco, com o assunto: " + dto.getAssunto());
        notificacao.setGravidade(GravidadeNfseDTO.INFORMACAO);
        notificacao.setTitulo(TipoNotificacaoNfseDTO.FALE_CONOSCO_PORTAL_NFSE.getDescricao());
        notificacao.setTipoNotificacao(TipoNotificacaoNfseDTO.FALE_CONOSCO_PORTAL_NFSE);
        notificacao.setLink("/nfse/fale-conosco/ver/" + dto.getId() + "/");

        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/fale-conosco/notificar").toUriString();
        restTemplate.postForEntity(url, notificacao, NotificacaoNfseDTO.class);
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getDefaltSearchFields() {
        return null;
    }

    @Override
    public ParameterizedTypeReference<List<FaleConoscoNfseDTO>> getResponseTypeList() {
        return null;
    }

    @Override
    public ParameterizedTypeReference<FaleConoscoNfseDTO> getResponseType() {
        return null;
    }
}
