package br.com.webpublico.service;

import br.com.webpublico.domain.dto.CadastroImobiliarioNfseDTO;
import br.com.webpublico.domain.dto.CadastroImobiliarioSearchNfseDTO;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class CadastroImobiliarioService extends AbstractWPService<CadastroImobiliarioNfseDTO> {

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getDefaltSearchFields() {
        return null;
    }

    @Override
    public ParameterizedTypeReference<List<CadastroImobiliarioNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<CadastroImobiliarioNfseDTO>>() {
        };
    }

    public ParameterizedTypeReference<List<CadastroImobiliarioSearchNfseDTO>> getResponseTypeListSearch() {
        return new ParameterizedTypeReference<List<CadastroImobiliarioSearchNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<CadastroImobiliarioNfseDTO> getResponseType() {
        return null;
    }


    public ResponseEntity<CadastroImobiliarioNfseDTO> findByInscricao(String inscricao) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/cadastro-imobiliario/by-inscricao";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("inscricao", inscricao)
            .toUriString();

        return restTemplate.getForEntity(url, CadastroImobiliarioNfseDTO.class);

    }

    public ResponseEntity<List<CadastroImobiliarioSearchNfseDTO>> search(Pageable pageable, String search) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/cadastro-imobiliario/search";
        url = UriComponentsBuilder.fromUriString(url)
            .queryParam("search", search)
            .toUriString();

        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);

        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseTypeListSearch());
    }

    public ResponseEntity<CadastroImobiliarioNfseDTO> findById(Long id) {
        return restTemplate.getForEntity(urlsProperties.getWebpublicoPathNfse() + "/cadastro-imobiliario/" + id, CadastroImobiliarioNfseDTO.class);
    }
}
