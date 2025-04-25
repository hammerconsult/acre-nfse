package br.com.webpublico.service;

import br.com.webpublico.domain.dto.CnaeNfseDTO;
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
public class CnaeService extends AbstractWPService<CnaeNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(CnaeService.class);
//    @Inject
//    private CnaeRepository cnaeRepository;
//
//    @Override
//    protected AbstractWpSearchRepository<CnaeSearchDTO> getRepository() {
//        return cnaeRepository;
//    }

    public CnaeNfseDTO findByCodigo(String codigo) {
        return findByAtribute("codigo", codigo);
    }

    @Override
    public String getTableName() {
        return "CNAE";
    }

    @Override
    public String getDefaltSearchFields() {
        return "codigoCnae, descricaoDetalhada";
    }

    @Override
    public ParameterizedTypeReference<List<CnaeNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<CnaeNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<CnaeNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<CnaeNfseDTO>() {
        };
    }

    public ResponseEntity<List<CnaeNfseDTO>> findByEmpresa(Long id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/cnae-por-cadastro/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    public ResponseEntity<List<CnaeNfseDTO>> getPorServico(Pageable pageable, String filtro) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/cnae-por-servico/" + filtro;
        url = UriComponentsBuilder.fromUriString(url).queryParam("filtro", filtro).toUriString();
        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);
        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseTypeList());
    }
}
