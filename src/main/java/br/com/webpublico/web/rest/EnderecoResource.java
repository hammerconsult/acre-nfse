package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.service.CidadeService;
import br.com.webpublico.web.rest.dto.CepCorreio;
import br.com.webpublico.web.rest.dto.EnderecoDTO;
import io.micrometer.core.annotation.Timed;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Optional;

/**
 * REST controller for managing Cnae.
 */
@RestController
@RequestMapping("/api")
public class EnderecoResource {

    private final Logger log = LoggerFactory.getLogger(EnderecoResource.class);
    @Inject
    private CidadeService cidadeService;
    private RestTemplate restTemplate;

    @RequestMapping(value = "/cep/{cep}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnderecoDTO> get(@PathVariable String cep) {
        try {
            log.debug("REST request to Endedereco by cep : {}", cep);
            EnderecoDTO endereco = findInWsViaCep(cep);
            return Optional.ofNullable(endereco)
                    .map(end -> new ResponseEntity<>(
                            end,
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private EnderecoDTO findInWsViaCep(String cep) throws IOException {
        try {
            CepCorreio cepCorreio = getRestTemplate().getForObject("https://viacep.com.br/ws/" + cep + "/json/", CepCorreio.class);
            return buildByCepCorreio(cepCorreio);
        } catch (Exception e) {
            return new EnderecoDTO();
        }
    }


    public EnderecoDTO buildByCepCorreio(CepCorreio cepCorreio) {
        EnderecoDTO endereco = new EnderecoDTO();
        endereco.setCep(cepCorreio.getCep());
        endereco.setLogradouro(cepCorreio.getLogradouro());
        endereco.setBairro(cepCorreio.getBairro());
        if (cepCorreio.getIbge() != null && !cepCorreio.getIbge().isEmpty()) {
            MunicipioNfseDTO municipio = cidadeService.findByCodigoIBGE(cepCorreio.getIbge());
            if (municipio != null) {
                endereco.setMunicipio(municipio);
            } else {
                endereco.setMunicipio(new MunicipioNfseDTO(cepCorreio.getLocalidade(), cepCorreio.getUf()));
            }
        }
        return endereco;
    }

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = null;
            try {
                sslContext = org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();
            } catch (Exception e) {
                log.error("erro ao consultar cep", e);
            }

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);
            restTemplate = new RestTemplate(requestFactory);
        }
        return restTemplate;
    }

}
