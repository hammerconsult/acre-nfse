package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArquivoJDBCRepository extends AbstractJDBCRepository<ArquivoNfseDTO> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public String getSelect() {
        return " select id, descricao, mimetype, nome, tamanho ";
    }

    @Override
    public String getFrom() {
        return " from arquivo obj ";
    }

    @Override
    public RowMapper<ArquivoNfseDTO> newRowMapper() {
        return new ArquivoNfseDTO();
    }

    @Override
    public ArquivoNfseDTO insert(ArquivoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivo (id, descricao, mimetype, nome, tamanho) " +
                " values (?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    public ArquivoNfseDTO findByLogin(String login) {
        String sql = "select arquivo.id, arquivo.descricao, arquivo.mimetype, arquivo.nome, arquivo.tamanho" +
                " from pessoa " +
                " inner join arquivo on arquivo.id = pessoa.ARQUIVO_ID " +
                " inner join USUARIOWEB usu on usu.PESSOA_ID = pessoa.id " +
                " where usu.login = '" + login + "' ";
        try {
            return jdbcTemplate
                    .queryForObject(sql, newRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            log.debug("Erro ao recuperar imagem por login.", e);
            log.error("Erro ao recuperar imagem por login.");
            return null;
        }
    }


    @Override
    public ArquivoNfseDTO update(ArquivoNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoNfseDTO dto) {
    }
}
