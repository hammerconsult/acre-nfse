package br.com.webpublico.service;

import br.com.webpublico.domain.dto.DocumentoFiscalDTO;
import br.com.webpublico.repository.DocumentoFiscalJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentoFiscalService extends AbstractService<DocumentoFiscalDTO> {

    @Autowired
    DocumentoFiscalJDBCRepository repository;

    @Override
    public DocumentoFiscalJDBCRepository getRepository() {
        return repository;
    }

}
