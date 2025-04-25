package br.com.webpublico.repository;

import br.com.webpublico.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Repository
public class LogGeralNfseJDBCRepository implements Serializable {


    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void inserir(String conteudo, String usuario, String tipo, Long idRelacionamento, HttpServletRequest request) {
        jdbcTemplate.batchUpdate(" INSERT INTO LOGGERALNFSE (ID, CONTEUDO, USUARIO, DATAREGISTRO, TIPO, IDRELACIONAMENTO, LOCAL, IP)" +
                " VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + conteudo + "','" + usuario + "', current_timestamp , '" + tipo + "'," + idRelacionamento + ", 'Portal da Nfs-e', '"+ Util.getBaseUrl(request)+"')");
    }
}
