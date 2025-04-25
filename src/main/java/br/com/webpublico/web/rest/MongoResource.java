package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.MongoCollectionDTO;
import br.com.webpublico.domain.dto.MongoDBDTO;
import br.com.webpublico.service.MongoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MongoResource implements Serializable {

    @Autowired
    private MongoService mongoService;

    @RequestMapping(value = "/mongo/get-db",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MongoDBDTO> getDb() {
        return ResponseEntity.ok(mongoService.getDb());
    }

    @RequestMapping(value = "/mongo/get-collections",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MongoCollectionDTO>> getCollections() {
        return ResponseEntity.ok(mongoService.getCollections());
    }

    @RequestMapping(value = "/mongo/drop-db",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity dropDb(@RequestParam String dbName) {
        mongoService.dropDatabase(dbName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/mongo/drop-collection",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity dropCollection(@RequestParam String collectionName) {
        mongoService.dropCollection(collectionName);
        return new ResponseEntity(HttpStatus.OK);
    }
}
