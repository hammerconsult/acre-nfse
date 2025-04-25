package br.com.webpublico.service;

import br.com.webpublico.config.RabbitMQConfiguration;
import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.email.AnexoDTO;
import br.com.webpublico.domain.dto.email.EmailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Value("${queueEnviarEmail}")
    private String queueEnviarEmail;

    public void enviarEmail(String para, String assunto, String conteudo, Boolean html) {
        enviarEmail(para, assunto, conteudo, html, Lists.newArrayList());
    }

    public void enviarEmail(String para, String assunto, String conteudo, Boolean html,
                            List<AnexoDTO> anexos) {
        try {
            ConfiguracaoNfseDTO configuracao = configuracaoService.getConfiguracaoFromServer();
            rabbitTemplate.convertAndSend(queueEnviarEmail, new EmailDTO(para, assunto, conteudo, html, anexos,
                    configuracao.getProducao()));
        } catch (Exception e) {
            logger.error("Erro ao enviarEmail.", e);
        }
    }
}
