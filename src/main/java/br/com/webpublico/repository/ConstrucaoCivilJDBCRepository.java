package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConstrucaoCivilLocalizacaoNfseDTO;
import br.com.webpublico.domain.dto.ConstrucaoCivilNfseDTO;
import br.com.webpublico.domain.dto.IntermediarioServicoNfseDTO;
import br.com.webpublico.repository.mapper.ConstrucaoCivilMapper;
import br.com.webpublico.repository.mapper.IntermediarioMapper;
import br.com.webpublico.repository.setter.ConstrucaoCivilLocalizacaoSetter;
import br.com.webpublico.repository.setter.ConstrucaoCivilSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ConstrucaoCivilJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IdJDBCRepository idJDBCRepository;

    public ConstrucaoCivilNfseDTO findById(Long id) {
        List<ConstrucaoCivilNfseDTO> query = jdbcTemplate.query(" SELECT ID,    " +
                        "       ART,    " +
                        "       NUMEROALVARA,    " +
                        "       RESPONSAVEL_ID,    " +
                        "       CONSTRUCAOCIVILLOCALIZACAO_ID,    " +
                        "       DATAAPROVACAOPROJETO,    " +
                        "       DATAINICIO,    " +
                        "       DATACONCLUSAO,    " +
                        "       INCORPORACAO,    " +
                        "       MATRICULAIMOVEL,    " +
                        "       CADASTROIMOBILIARIO_ID,    " +
                        "       NUMEROHABITESE,    " +
                        "       CONSTRUCAOCIVILSTATUS,    " +
                        "       CODIGOOBRA,    " +
                        "       CADASTROECONOMICO_ID   " +
                        "FROM CONSTRUCAOCIVIL where ID = ? ",
                new Object[]{id},
                new ConstrucaoCivilMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public void inserir(ConstrucaoCivilNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO CONSTRUCAOCIVIL " +
                " (ID, ART, NUMEROALVARA, RESPONSAVEL_ID, CONSTRUCAOCIVILLOCALIZACAO_ID, DATAAPROVACAOPROJETO, " +
                " DATAINICIO, DATACONCLUSAO, INCORPORACAO, MATRICULAIMOVEL, CADASTROIMOBILIARIO_ID, NUMEROHABITESE, " +
                " CONSTRUCAOCIVILSTATUS, CODIGOOBRA, CADASTROECONOMICO_ID) " +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new ConstrucaoCivilSetter(dto));
    }

    public void inserirLocalizacao(ConstrucaoCivilLocalizacaoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO CONSTRUCAOCIVILLOCALIZACAO " +
                        " (ID, CIDADE_ID, LOGRADOURO, NUMERO, BAIRRO, CEP, NOMEEMPREENDIMENTO) " +
                        " VALUES(?, ?, ?, ?, ?, ?, ?)  ",
                new ConstrucaoCivilLocalizacaoSetter(dto));
    }
}
