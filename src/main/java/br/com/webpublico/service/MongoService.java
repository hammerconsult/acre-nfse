package br.com.webpublico.service;

import br.com.webpublico.domain.dto.MongoCollectionDTO;
import br.com.webpublico.domain.dto.MongoDBDTO;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MongoService implements Serializable {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void dropCollection(String collectionName) {
        mongoTemplate.getDb().getName();
        if (!mongoTemplate.collectionExists(collectionName))
            throw new NfseOperacaoNaoPermitidaException("Colecction inexistente!");
        mongoTemplate.dropCollection(collectionName);
    }

    public void dropDatabase(String dbName) {
        if (!dbName.equals(mongoTemplate.getDb().getName()))
            throw new NfseOperacaoNaoPermitidaException("Database incorreto!");
        mongoTemplate.getDb().drop();
    }

    public MongoDBDTO getDb() {
        MongoDBDTO dto = new MongoDBDTO();
        dto.setName(mongoTemplate.getDb().getName());
        return dto;
    }

    public List<MongoCollectionDTO> getCollections() {
        return mongoTemplate.getCollectionNames().stream().map(name -> {
            MongoCollectionDTO dto = new MongoCollectionDTO();
            dto.setName(name);
            return dto;
        }).collect(Collectors.toList());
    }
}
