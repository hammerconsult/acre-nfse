package br.com.webpublico.service;

import br.com.webpublico.domain.dto.TipoLegislacaoDTO;
import br.com.webpublico.domain.dto.TipoLegislacaoLegislacaoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Created by wellington on 17/10/17.
 */
@Service
public class TipoLegislacaoService extends AbstractWPService<TipoLegislacaoDTO> {
    @Override
    public String getTableName() {
        return "TipoLegislacao";
    }

    @Override
    public String getDefaltSearchFields() {
        return "descricao";
    }

    public ResponseEntity<List<TipoLegislacaoLegislacaoDTO>> buscarTiposLegislacaoComLegislacoes() {
        UriComponentsBuilder uri = fromUriString(urlsProperties.getWebpublicoPathNfse() + "/tipo-legislacao-com-legislacoes");
        ResponseEntity<TipoLegislacaoLegislacaoDTO[]> response = restTemplate.getForEntity(uri.toUriString(), TipoLegislacaoLegislacaoDTO[].class);
        return new ResponseEntity<>(Arrays.asList(response.getBody()), response.getHeaders(), HttpStatus.OK);
    }


    @Override
    public ParameterizedTypeReference<List<TipoLegislacaoDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<TipoLegislacaoDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<TipoLegislacaoDTO> getResponseType() {
        return new ParameterizedTypeReference<TipoLegislacaoDTO>() {
        };
    }
}
