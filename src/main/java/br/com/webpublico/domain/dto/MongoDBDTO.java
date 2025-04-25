package br.com.webpublico.domain.dto;

import java.io.Serializable;

public class MongoDBDTO implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
