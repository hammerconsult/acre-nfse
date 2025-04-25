package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConfiguracaoDeRelatorioNfseDTO;
import br.com.webpublico.domain.dto.WebReportDTO;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.webreportdto.dto.comum.RelatorioDTO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WebReportService {

    private static final Logger log = LoggerFactory.getLogger(WebReportService.class);

    @Autowired
    private ConfiguracaoDeRelatorioService configuracaoDeRelatorioService;
    private Map<String, Map<String, WebReportDTO>> relatorios = Maps.newHashMap();
    @Autowired
    @Qualifier("restTemplateWebReport")
    private RestTemplate restTemplate;


    public WebReportDTO gerarRelatorio(String usuario, RelatorioDTO dto) throws NfseOperacaoNaoPermitidaException {
        ConfiguracaoDeRelatorioNfseDTO configuracaoDeRelatorio = configuracaoDeRelatorioService.getConfiguracaoDeRelatorio();
        iniciarCacheRelatorioUsuario(usuario);
        return solicitarGeracaoRelatorio(usuario, dto, configuracaoDeRelatorio);
    }

    private void iniciarCacheRelatorioUsuario(String usuario) {
        if (!relatorios.containsKey(usuario)) {
            relatorios.put(usuario, Maps.newHashMap());
        }
    }

    private WebReportDTO solicitarGeracaoRelatorio(String usuario, RelatorioDTO dto, ConfiguracaoDeRelatorioNfseDTO configuracao) {
        dto.setLoginUsuario(usuario);
        dto.setUrlWebpublico(configuracao.getUrlWebpublico());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RelatorioDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<UUID> responseEntity = restTemplate.exchange(configuracao.getUrlCompleta(dto.getApi() + "gerar"),
                HttpMethod.POST, request, UUID.class);
        if (!responseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            UUID uuid = responseEntity.getBody();
            WebReportDTO webReportDTO = new WebReportDTO(dto.getNomeRelatorio(), usuario, uuid.toString(), null, dto);
            relatorios.get(usuario).put(uuid.toString(), webReportDTO);
            log.debug("ta gerando ..." + uuid);
            return webReportDTO;
        }
        return null;
    }

    public void porcentagemRelatorio(String uuid, BigDecimal porcentagem) {
        log.debug("porcentagem do relatorio ..." + porcentagem + " : " + uuid);

        for (String usuario : relatorios.keySet()) {
            if (relatorios.get(usuario).containsKey(uuid)) {
                relatorios.get(usuario).get(uuid).setPorcentagem(porcentagem);
                log.debug("alterou porcentagem ..." + uuid);
                break;
            }
        }
    }

    public void publicarRelatorio(String uuid) {
        log.debug("Ta chengando ..." + uuid);
        ConfiguracaoDeRelatorioNfseDTO configuracao = configuracaoDeRelatorioService.getConfiguracaoDeRelatorio();

        String urlConsulta = UriComponentsBuilder
                .fromUriString(configuracao.getUrlCompleta("relatorio/imprimir"))
                .queryParam("uuid", uuid)
                .build()
                .toUriString();

        ResponseEntity<byte[]> response = restTemplate
                .getForEntity(urlConsulta, byte[].class);

        if (!response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            for (String usuario : relatorios.keySet()) {
                if (relatorios.get(usuario).containsKey(uuid)) {
                    relatorios.get(usuario).get(uuid).setConteudo(response.getBody());
                    relatorios.get(usuario).get(uuid).setFim(System.currentTimeMillis());
                    log.debug("Chegou ..." + uuid);
                    break;
                }
            }
        } else {
            for (String usuario : relatorios.keySet()) {
                if (relatorios.get(usuario).containsKey(uuid)) {
                    List<String> erros = response.getHeaders().get(WebReportDTO.HEADER_MESSAGE_ERROR);
                    if (erros != null && !erros.isEmpty()) {
                        relatorios.get(usuario).get(uuid).setErro(erros.get(0));
                    }
                    log.debug("Chegou com erro ..." + uuid);
                    break;
                }
            }
        }

    }

    public WebReportDTO getByUuid(String uuid) {
        for (String usuario : relatorios.keySet()) {
            if (relatorios.get(usuario).containsKey(uuid)) {
                return relatorios.get(usuario).get(uuid);
            }
        }
        return null;
    }

    public void imprimir(HttpServletResponse response, String uuid) throws IOException {
        WebReportDTO webReport = getByUuid(uuid);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + webReport.getNome() + ".pdf");
        response.setContentLength(webReport.getConteudo().length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(webReport.getConteudo(), 0, webReport.getConteudo().length);
        outputStream.flush();
        outputStream.close();
    }
}
