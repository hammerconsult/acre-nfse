package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.ArquivoBase64DTO;
import br.com.webpublico.domain.dto.EstatisticaMensalNfseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstatisticaMensalMongoRepository extends MongoRepository<EstatisticaMensalNfseDTO, String> {

}

