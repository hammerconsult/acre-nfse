package br.com.webpublico.service;

import br.com.webpublico.domain.dto.PerguntasRespostasDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
public class PerguntasRespostasService extends AbstractWPService<PerguntasRespostasDTO> {

    private final Logger log = LoggerFactory.getLogger(PerguntasRespostasService.class);

    @Override
    public String getTableName() {
        return "PerguntasRespostas";
    }

    @Override
    public String getDefaltSearchFields() {
        return "pergunta, resposta";
    }

    public ResponseEntity<List<PerguntasRespostasDTO>> buscarPerguntasRespostasParaExibicao() {
        UriComponentsBuilder uri = fromUriString(urlsProperties.getWebpublicoPathNfse() + "/perguntas-respostas-para-exibicao");
        ResponseEntity<PerguntasRespostasDTO[]> response = restTemplate.getForEntity(uri.toUriString(), PerguntasRespostasDTO[].class);
        return new ResponseEntity(Arrays.asList(response.getBody()), response.getHeaders(), HttpStatus.OK);
    }

    @Override
    public ParameterizedTypeReference<List<PerguntasRespostasDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<PerguntasRespostasDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<PerguntasRespostasDTO> getResponseType() {
        return new ParameterizedTypeReference<PerguntasRespostasDTO>() {
        };
    }
}
