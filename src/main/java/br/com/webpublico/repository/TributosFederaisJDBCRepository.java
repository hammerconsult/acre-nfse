package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.TributosFederaisNfseDTO;
import br.com.webpublico.repository.mapper.TributosFederaisMapper;
import br.com.webpublico.repository.setter.DadosPessoaisSetter;
import br.com.webpublico.repository.setter.TributosFederaisSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class TributosFederaisJDBCRepository implements Serializable {

    private static final String SELECT = " SELECT T.ID, T.PIS, T.COFINS, T.INSS, T.IRRF, T.CSLL, T.OUTRASRETENCOES, " +
            " T.CPP, T.RETENCAOPIS, T.RETENCAOCOFINS, T.RETENCAOINSS, T.RETENCAOIRRF, T.RETENCAOCSLL, " +
            " T.RETENCAOCPP, T.RETENCAOOUTRASRETENCOES, T.PERCENTUALPIS, T.PERCENTUALCOFINS, " +
            " T.PERCENTUALINSS, T.PERCENTUALIRRF, T.PERCENTUALCSLL, T.PERCENTUALCPP, " +
            " T.PERCENTUALOUTRASRETENCOES ";
    private static final String FROM = " FROM TRIBUTOSFEDERAIS T ";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public void inserir(TributosFederaisNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("INSERT INTO TRIBUTOSFEDERAIS " +
                        "(ID, PIS, COFINS, INSS, IRRF, CSLL, OUTRASRETENCOES, CPP,  " +
                        "RETENCAOPIS, RETENCAOCOFINS, RETENCAOINSS, RETENCAOIRRF,  " +
                        "RETENCAOCSLL, RETENCAOCPP, RETENCAOOUTRASRETENCOES,  " +
                        "PERCENTUALPIS, PERCENTUALCOFINS, PERCENTUALINSS, PERCENTUALIRRF,  " +
                        "PERCENTUALCSLL, PERCENTUALCPP, PERCENTUALOUTRASRETENCOES) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new TributosFederaisSetter(dto, false));
    }

    public void atualizar(TributosFederaisNfseDTO dto) {
        jdbcTemplate.batchUpdate(" UPDATE TRIBUTOSFEDERAIS SET PIS = ?, COFINS = ?," +
                        " INSS = ?, IRRF = ?, CSLL = ?, OUTRASRETENCOES = ?, CPP = ?,  " +
                        " RETENCAOPIS = ?, RETENCAOCOFINS = ?, RETENCAOINSS = ?, RETENCAOIRRF = ?,  " +
                        " RETENCAOCSLL = ?, RETENCAOCPP = ?, RETENCAOOUTRASRETENCOES = ?,  " +
                        " PERCENTUALPIS = ?, PERCENTUALCOFINS = ?, PERCENTUALINSS = ?, PERCENTUALIRRF = ?,  " +
                        " PERCENTUALCSLL = ?, PERCENTUALCPP = ?, PERCENTUALOUTRASRETENCOES = ? " +
                        " WHERE ID = ? ",
                new TributosFederaisSetter(dto, true));
    }

    public TributosFederaisNfseDTO findById(Long id) {
        List<TributosFederaisNfseDTO> query = jdbcTemplate.query(SELECT + FROM +
                        " WHERE T.ID = ? ",
                new Object[]{id},
                new TributosFederaisMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public TributosFederaisNfseDTO findByPrestadorId(Long prestadorId) {
        List<TributosFederaisNfseDTO> query = jdbcTemplate.query(SELECT + FROM +
                        " INNER JOIN CADASTROECONOMICO CE ON CE.TRIBUTOSFEDERAIS_ID = T.ID " +
                        " WHERE CE.ID = ? ",
                new Object[]{prestadorId},
                new TributosFederaisMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
