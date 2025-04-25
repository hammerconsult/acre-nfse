package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.ImagemUsuarioNfseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImagemUsuarioMongoRepository extends MongoRepository<ImagemUsuarioNfseDTO, Long> {

     ImagemUsuarioNfseDTO findByPessoaId(Long id);

}

