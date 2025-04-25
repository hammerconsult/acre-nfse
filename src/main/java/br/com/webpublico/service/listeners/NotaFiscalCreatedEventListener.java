package br.com.webpublico.service.listeners;

import br.com.webpublico.config.RabbitMQConfiguration;
import br.com.webpublico.domain.events.NotaFiscalCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotaFiscalCreatedEventListener {

    private final Logger log = LoggerFactory.getLogger(NotaFiscalCreatedEventListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processNotaFiscalCreatedEvent(NotaFiscalCreatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.queueIntegrarNotaFiscal, event.getDto().getId().toString());
    }

}
