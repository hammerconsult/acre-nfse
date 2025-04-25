package br.com.webpublico.service;

import br.com.webpublico.domain.dto.NaturezaJuridicaNfseDTO;
import br.com.webpublico.repository.NaturezaJuridicaJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class NaturezaJuridicaService extends AbstractWPService<NaturezaJuridicaNfseDTO> {

    @Autowired
    private NaturezaJuridicaJDBCRepository naturezaJuridicaJDBCRepository;

    public ResponseEntity<List<NaturezaJuridicaNfseDTO>> buscarNaturezaJuridica() {
        String url = urlsProperties.getWebpublicoPathNfse() + "/naturezas-juridicas";
        url = UriComponentsBuilder.fromUriString(url).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    public ResponseEntity<List<NaturezaJuridicaNfseDTO>> buscarNaturezaFisica() {
        String url = urlsProperties.getWebpublicoPathNfse() + "/naturezas-fisicas";
        url = UriComponentsBuilder.fromUriString(url).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    @Override
    public String getTableName() {
        return "NaturezaJuridica";
    }

    @Override
    public String getDefaltSearchFields() {
        return "descricao";
    }

    @Override
    public ParameterizedTypeReference<List<NaturezaJuridicaNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<NaturezaJuridicaNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<NaturezaJuridicaNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<NaturezaJuridicaNfseDTO>() {
        };
    }

    public NaturezaJuridicaNfseDTO findById(Long id) {
        return naturezaJuridicaJDBCRepository.findById(id);
    }
}
