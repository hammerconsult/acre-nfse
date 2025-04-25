package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConstrucaoCivilNfseDTO;
import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.repository.mapper.ConstrucaoCivilMapper;
import br.com.webpublico.repository.mapper.DadosPessoaisMapper;
import br.com.webpublico.repository.setter.DadosPessoaisSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class DadosPessoaisJDBCRepository implements Serializable {

    private static final String SELECT = " SELECT ID, INSCRICAOMUNICIPAL," +
            " INSCRICAOESTADUALRG, NOMERAZAOSOCIAL, NOMEFANTASIA, EMAIL, TELEFONE, " +
            " CELULAR, CEP, NUMERO, LOGRADOURO, BAIRRO, COMPLEMENTO, MUNICIPIO," +
            " UF, APELIDO, PAIS, NUMEROIDENTIFICACAO, CODIGOMUNICIPIO, CODIGOPAIS, " +
            " CPFCNPJ, TIPOISSQN, REGIMETRIBUTARIO ";
    private static final String FROM = " FROM DADOSPESSOAISNFSE  ";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public void inserirDadosPessoais(DadosPessoaisNfseDTO dto) {
        if (dto != null) {
            dto.setId(idJDBCRepository.getId());
            jdbcTemplate.batchUpdate("INSERT INTO DADOSPESSOAISNFSE  " +
                            "(ID, INSCRICAOMUNICIPAL, INSCRICAOESTADUALRG, NOMERAZAOSOCIAL,   " +
                            "NOMEFANTASIA, EMAIL, TELEFONE, CELULAR, CEP, NUMERO, LOGRADOURO, BAIRRO,   " +
                            "COMPLEMENTO, MUNICIPIO, UF, CODIGOMUNICIPIO, PAIS, CODIGOPAIS,   " +
                            "APELIDO, NUMEROIDENTIFICACAO, CPFCNPJ, TIPOISSQN, REGIMETRIBUTARIO)  " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new DadosPessoaisSetter(dto, false));
        }
    }

    public void atualizarDadosPessoais(DadosPessoaisNfseDTO dto) {
        if (dto != null) {
            jdbcTemplate.batchUpdate(" UPDATE DADOSPESSOAISNFSE SET INSCRICAOMUNICIPAL = ?, INSCRICAOESTADUALRG = ?, " +
                            " NOMERAZAOSOCIAL = ?, NOMEFANTASIA = ?, EMAIL = ?, TELEFONE = ?, CELULAR = ?, CEP = ?, " +
                            " NUMERO = ?, LOGRADOURO = ?, BAIRRO = ?, COMPLEMENTO = ?, MUNICIPIO = ?, " +
                            " UF = ?, CODIGOMUNICIPIO = ?, PAIS = ?, CODIGOPAIS = ?, APELIDO = ?, NUMEROIDENTIFICACAO = ?," +
                            " CPFCNPJ = ? " +
                            " WHERE ID = ? ",
                    new DadosPessoaisSetter(dto, true));
        }
    }

    public DadosPessoaisNfseDTO findById(Long id) {
        List<DadosPessoaisNfseDTO> query = jdbcTemplate.query(
                        SELECT + FROM +
                        " WHERE ID = ? ",
                new Object[]{id},
                new DadosPessoaisMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public DadosPessoaisNfseDTO findByCpfCnpj(String cpfCnpj) {
        List<DadosPessoaisNfseDTO> query = jdbcTemplate.query(
                SELECT + FROM +
                        " WHERE CPFCNPJ = ? ",
                new Object[]{cpfCnpj},
                new DadosPessoaisMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
