package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotaFiscalMongoRepository extends MongoRepository<NotaFiscalNfseDTO, Long> {

    Page<NotaFiscalNfseDTO> findByEnviouPorEmail(Boolean enviou, Pageable pageable);


    Page<NotaFiscalNfseDTO> findByPrestador_Id(Long prestadorId, Pageable pageable);

}

