package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadastroEconomicoMongoRepository extends MongoRepository<PrestadorServicoNfseDTO, Long> {

}

