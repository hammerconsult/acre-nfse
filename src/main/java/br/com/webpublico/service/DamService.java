package br.com.webpublico.service;

import br.com.webpublico.config.UrlsProperties;
import br.com.webpublico.domain.dto.AssistenteDAM;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.repository.DamJDBCRepository;
import br.com.webpublico.tributario.consultadebitos.dtos.DamDTO;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DamService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DamService.class);

    @Autowired
    private DamJDBCRepository damJDBCRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UrlsProperties urlsProperties;

    public DamDTO buscarUltimoDamParcela(Long idParcela) {
        return damJDBCRepository.buscarUltimoDamParcela(idParcela);
    }

    public void imprimirDam(List<Long> idsParcelas, HttpServletResponse response) {
        try {
            List<Long> idsDams = gerarDAMs(idsParcelas);
            byte[] dados = gerarBytesImpressaoDAM(idsDams);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=dam.pdf");
            response.setContentLength(dados.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(dados, 0, dados.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao gerar DAM ", e);
            throw new NfseOperacaoNaoPermitidaException("O serviço de impressão de DAM não está disponível no momento.");
        }
    }

    private byte[] gerarBytesImpressaoDAM(List<Long> idsDams) {
        AssistenteDAM assistenteDAM = new AssistenteDAM();
        assistenteDAM.setIdsDam(idsDams);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AssistenteDAM> httpEntity = new HttpEntity<>(assistenteDAM, httpHeaders);
        ResponseEntity<byte[]> responseImpressao = restTemplate.exchange(urlsProperties.getDamService() + "/api/imprimir-dam-unico",
                HttpMethod.POST, httpEntity, byte[].class);
        return responseImpressao.getBody();
    }

    private List<Long> gerarDAMs(List<Long> idsParcelas) {
        return idsParcelas.stream()
                .map(this::gerarDAM)
                .collect(Collectors.toList());
    }

    private Long gerarDAM(Long idParcela) {
        AssistenteDAM assistenteDAM = new AssistenteDAM();
        assistenteDAM.setIdParcela(idParcela);
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(urlsProperties.getDamService() +
                "/api/gerar-dam-unico", assistenteDAM, Long.class);
        return responseEntity.getBody();
    }
}
