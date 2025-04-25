package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.UserNfseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioWebMongoRepository extends MongoRepository<UserNfseDTO, Long> {

    UserNfseDTO findByLogin(String login);

    UserNfseDTO findByPessoaId(Long id);

}

