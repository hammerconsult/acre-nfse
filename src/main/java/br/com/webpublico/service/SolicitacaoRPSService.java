package br.com.webpublico.service;

import br.com.webpublico.domain.dto.SolicitacaoRPSNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoSolicitacaoRPSNfseDTO;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
public class SolicitacaoRPSService extends AbstractWPService<SolicitacaoRPSNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoRPSService.class);

    public ResponseEntity<SolicitacaoRPSNfseDTO> buscarSolicitacaoRPSPorIdAndPrestador(Long aidfe_id, Long prestador_id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/solicitacao-rps";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("id", aidfe_id)
            .queryParam("prestadorId", prestador_id).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseType());
    }

    public ResponseEntity<List<SolicitacaoRPSNfseDTO>> buscarSolicitacaoRPSPorPrestadorServico(Long prestadorId, String filtro, Pageable pageable) {
        if (pageable == null) {
            pageable = PaginationUtil.generatePageRequest(0, 100);
        }
        String url = urlsProperties.getWebpublicoPathNfse() + "/solicitacao-rps-por-empresa";
        url = UriComponentsBuilder.fromUriString(url).queryParam("prestadorId", prestadorId).queryParam("filtro", filtro).toUriString();
        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);
        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseTypeList());
    }


    public SolicitacaoRPSNfseDTO salvarSolicitacaoRPS(SolicitacaoRPSNfseDTO dto) {
        dto.setSituacao(SituacaoSolicitacaoRPSNfseDTO.AGUARDANDO);
        dto.setSolicitadaEm(new Date());
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/solicitacao-rps").toUriString();
        ResponseEntity<SolicitacaoRPSNfseDTO> response = restTemplate.postForEntity(url, dto, SolicitacaoRPSNfseDTO.class);
        return response.getBody();
    }

    @Override
    public String getTableName() {
        return "SolicitacaoRPS";
    }

    @Override
    public String getDefaltSearchFields() {
        return "";
    }

    @Override
    public ParameterizedTypeReference<List<SolicitacaoRPSNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<SolicitacaoRPSNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<SolicitacaoRPSNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<SolicitacaoRPSNfseDTO>() {
        };
    }
}
