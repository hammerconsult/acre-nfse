package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.ArquivoBase64DTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArquivoBase64MongoRepository extends MongoRepository<ArquivoBase64DTO, Long> {

}

