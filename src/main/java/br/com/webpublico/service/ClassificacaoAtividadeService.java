package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ClassificacaoAtividadeNfseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ClassificacaoAtividadeService extends AbstractWPService<ClassificacaoAtividadeNfseDTO> {


    public ResponseEntity<List<ClassificacaoAtividadeNfseDTO>> buscarClassificacoes() {
        String url = urlsProperties.getWebpublicoPathNfse() + "/classificacoes";
        url = UriComponentsBuilder.fromUriString(url).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    @Override
    public String getTableName() {
        return "ClassificacaoAtividade";
    }

    @Override
    public String getDefaltSearchFields() {
        return "descricao";
    }

    @Override
    public ParameterizedTypeReference<List<ClassificacaoAtividadeNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<ClassificacaoAtividadeNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<ClassificacaoAtividadeNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<ClassificacaoAtividadeNfseDTO>() {
        };
    }
}
