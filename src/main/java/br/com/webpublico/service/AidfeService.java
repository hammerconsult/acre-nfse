package br.com.webpublico.service;

import br.com.webpublico.domain.dto.AidfeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoAidfeNfseDTO;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class AidfeService extends AbstractWPService<AidfeNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(AidfeService.class);

    public ResponseEntity<AidfeNfseDTO> buscarAIDFePorIdAndPrestador(Long aidfe_id, Long prestador_id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/aidfe";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("id", aidfe_id)
            .queryParam("prestadorId", prestador_id).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseType());
    }

    public ResponseEntity<List<AidfeNfseDTO>> buscarAidfePorPrestadorServico(Long prestadorId, String filtro, Pageable pageable) {
        if (pageable == null) {
            pageable = PaginationUtil.generatePageRequest(0, 100);
        }
        String url = urlsProperties.getWebpublicoPathNfse() + "/aidfe-por-empresa";
        url = UriComponentsBuilder.fromUriString(url).queryParam("prestadorId", prestadorId).queryParam("filtro", filtro).toUriString();
        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);
        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseTypeList());
    }

    public Long buscarNumeroInicialNFSE(Long prestadorId) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/aidfe-obter-proximo-numero-inicial-por-empresa";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("prestadorId", prestadorId).toUriString();
        ResponseEntity<Long> exchange = restTemplate.exchange(url, HttpMethod.GET, null, Long.class);
        if (exchange.getBody() != null) {
            return exchange.getBody();
        }
        return 0L;
    }

    public AidfeNfseDTO buscarAidfeWithStatusAguardando(Long prestadorId) {
        Pageable pageable = PaginationUtil.generatePageRequest(0, 1);
        String url = urlsProperties.getWebpublicoPathNfse() + "/recuperar-aidfe-por-empresa-e-situacao";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("prestadorId", prestadorId)
            .queryParam("situacao", SituacaoAidfeNfseDTO.AGUARDANDO.name())
            .toUriString();
        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);
        ResponseEntity<List<AidfeNfseDTO>> exchange = restTemplate.exchange(uriComponentsBuilder.toUriString(),
            HttpMethod.GET, null, getResponseTypeList());
        if (exchange.getBody() != null && !exchange.getBody().isEmpty()) {
            return exchange.getBody().get(0);
        }
        return null;
    }

    public AidfeNfseDTO salvarAidfe(AidfeNfseDTO aidfe) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/aidfe").toUriString();
        ResponseEntity<AidfeNfseDTO> response = restTemplate.postForEntity(url, aidfe, AidfeNfseDTO.class);
        return response.getBody();
    }

    @Override
    public String getTableName() {
        return "Aidfe";
    }

    @Override
    public String getDefaltSearchFields() {
        return "";
    }

    @Override
    public ParameterizedTypeReference<List<AidfeNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<AidfeNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<AidfeNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<AidfeNfseDTO>() {
        };
    }
}
