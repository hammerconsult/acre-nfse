package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.XmlNotaFiscalDTO;
import br.com.webpublico.domain.dto.XmlWsNfseRecebido;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface XmlWsNfseMongoRepository extends MongoRepository<XmlWsNfseRecebido, Long> {

}

