package br.com.webpublico.repository.mongo;

import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotaFiscalSearchMongoRepository extends MongoRepository<NotaFiscalSearchDTO, Long> {


    Page<NotaFiscalSearchDTO> findByidPrestadorOrderByIdDesc(Long idPrestador, Pageable pageable);

}

