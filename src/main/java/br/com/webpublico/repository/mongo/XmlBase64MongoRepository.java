package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.XmlNotaFiscalDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface XmlBase64MongoRepository extends MongoRepository<XmlNotaFiscalDTO, Long> {

}

