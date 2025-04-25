package br.com.webpublico.service;

import br.com.webpublico.domain.dto.NotificacaoNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.NotificacaoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificacaoService extends AbstractService<NotificacaoNfseDTO> {

    @Autowired
    private NotificacaoJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }
}
