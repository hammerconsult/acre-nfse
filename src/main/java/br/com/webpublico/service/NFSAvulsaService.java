package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.domain.dto.NFSAvulsaNfseDTO;
import br.com.webpublico.domain.dto.NFSAvulsaSearchNfseDTO;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class NFSAvulsaService extends AbstractWPService<NFSAvulsaNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(NFSAvulsaService.class);

    public NFSAvulsaNfseDTO save(NFSAvulsaNfseDTO notaFiscalAvulsa) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/nota-fiscal-avulsa").toUriString();
        ResponseEntity<NFSAvulsaNfseDTO> response = restTemplate.postForEntity(url, notaFiscalAvulsa, NFSAvulsaNfseDTO.class);
        return response.getBody();
    }

    public ResponseEntity<List<NFSAvulsaSearchNfseDTO>> buscarNFSAvulsa(ConsultaGenericaNfseDTO consultaGenerica) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/nota-fiscal-avulsa/search";
        url = UriComponentsBuilder.fromUriString(url)
                .toUriString();
        ResponseEntity<NFSAvulsaSearchNfseDTO[]> exchange = restTemplate.postForEntity(url, consultaGenerica, NFSAvulsaSearchNfseDTO[].class);
        List<NFSAvulsaSearchNfseDTO> retorno = Arrays.asList(exchange.getBody());
        return new ResponseEntity(retorno, exchange.getHeaders(), exchange.getStatusCode());
    }

    public ParameterizedTypeReference<List<NFSAvulsaSearchNfseDTO>> getResponseTypeSearchList() {
        return new ParameterizedTypeReference<List<NFSAvulsaSearchNfseDTO>>() {
        };
    }


    public NFSAvulsaNfseDTO findById(Long id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/nota-fiscal-avulsa/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseType()).getBody();
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
    public ParameterizedTypeReference<List<NFSAvulsaNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<NFSAvulsaNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<NFSAvulsaNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<NFSAvulsaNfseDTO>() {
        };
    }

    public void imprimirNotaFiscalAvulsa(HttpServletResponse response, Long prestador, Long id) {
        try {
            String url = urlsProperties.getWebpublicoPathNfse() + "/nota-fiscal-avulsa/imprime/";
            url = UriComponentsBuilder.fromUriString(url).queryParam("table", getTableName())
                    .queryParam("id", id)
                    .toUriString();
            ResponseEntity<byte[]> exchange = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);
            byte[] bytes = exchange.getBody();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=NFSAvulsa.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Não foi possível imprimir a nota fiscal avulsa ", e);
        }
    }
}
