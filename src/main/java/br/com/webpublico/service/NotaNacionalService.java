package br.com.webpublico.service;

import br.com.webpublico.config.RabbitMQConfiguration;
import br.com.webpublico.domain.dto.GraficoQuantitativoNotaNacionalDTO;
import br.com.webpublico.domain.dto.IntegracaoNotaNacionalDTO;
import br.com.webpublico.domain.dto.MensagemDfeAdnDTO;
import br.com.webpublico.domain.dto.RetornoDfeAdnDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.repository.NotaNacionalJDBCRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaNacionalService {

    @Autowired
    private NotaNacionalJDBCRepository repository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void integrarNotaNacional(Long notaFiscalId) {
        if (!repository.existsIntegracaoComSucesso(notaFiscalId)) {
            rabbitTemplate.convertAndSend(RabbitMQConfiguration.queueIntegrarNotaFiscal, notaFiscalId.toString());
        }
    }

    public void reintegrarNotasPorCodigoErro(String codigoErro) {
        List<Long> ids = repository.buscarIdsNotasPorCodigoErro(codigoErro);
        for (Long id : ids) {
            rabbitTemplate.convertAndSend(RabbitMQConfiguration.queueIntegrarNotaFiscal, id.toString());
        }
    }

    public List<RetornoDfeAdnDTO> buscarRetornosDfeAdn(Long notaFiscalId) {
        return repository.buscarRetornosDfeAdn(notaFiscalId);
    }

    public List<MensagemDfeAdnDTO> buscarMensagensDfeAdn(Long retornoId) {
        return repository.buscarMensagensDfeAdn(retornoId);
    }

    public Page<IntegracaoNotaNacionalDTO> buscarIntegracoes(Pageable pageable,
                                                             List<ParametroQuery> parametros) throws Exception {
        List<IntegracaoNotaNacionalDTO> dtos = repository.buscarIntegracoes(pageable, parametros);
        Integer count = repository.contarIntegracoes(parametros);
        return new PageImpl<>(dtos, pageable, count);
    }

    public List<GraficoQuantitativoNotaNacionalDTO> buscarQuantidadePorErro() {
        return repository.buscarQuantidadePorErro();
    }

    public List<GraficoQuantitativoNotaNacionalDTO> buscarQuantidadePorStatus() {
        return repository.buscarQuantidadePorStatus();
    }
}
