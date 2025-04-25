package br.com.webpublico.service;

import br.com.webpublico.domain.dto.LinhaEventoSimplesNacionalNfseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class EventoSimplesNacionalService extends AbstractWPService<LinhaEventoSimplesNacionalNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(EventoSimplesNacionalService.class);

    public ResponseEntity<List<LinhaEventoSimplesNacionalNfseDTO>> buscarEventosPorEmpresa(Long prestadorId, String filtro) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/eventos-por-empresa";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("prestadorId", prestadorId)
            .queryParam("filtro", filtro)
            .toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    @Override
    public String getTableName() {
        return "";
    }

    @Override
    public String getDefaltSearchFields() {
        return "";
    }

    @Override
    public ParameterizedTypeReference<List<LinhaEventoSimplesNacionalNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<LinhaEventoSimplesNacionalNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<LinhaEventoSimplesNacionalNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<LinhaEventoSimplesNacionalNfseDTO>() {
        };
    }
}
