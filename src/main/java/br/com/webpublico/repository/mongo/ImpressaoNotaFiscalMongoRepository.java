package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.ImpressaoNotaFiscalNfseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImpressaoNotaFiscalMongoRepository extends MongoRepository<ImpressaoNotaFiscalNfseDTO, Long> {

}

