package br.com.webpublico.service;

import br.com.webpublico.repository.ExercicioJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ExercicioService implements Serializable {

    @Autowired
    ExercicioJDBCRepository repository;

    public Long getIdByAno(Integer ano) {
        return repository.getIdByAno(ano);
    }
}
