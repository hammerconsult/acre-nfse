package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.domain.dto.TelefoneNfseDTO;
import br.com.webpublico.repository.mapper.LongMapper;
import br.com.webpublico.repository.mapper.PessoaMapper;
import br.com.webpublico.repository.mapper.TelefoneMapper;
import br.com.webpublico.repository.setter.PessoaSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PessoaJDBCRepository implements Serializable {

    private static final String SQL_FIELDS = " SELECT P.ID AS ID,  " +
            "       CASE WHEN PF.ID IS NOT NULL THEN 'FISICA' ELSE 'JURIDICA' END TIPO_PESSOA, " +
            "       COALESCE(PF.CPF, PJ.CNPJ) AS CPF_CNPJ,  " +
            "       COALESCE(PF.NOME, PJ.RAZAOSOCIAL) AS NOME_RAZAOSOCIAL,  " +
            "       COALESCE('', PJ.NOMEFANTASIA) AS NOME_FANTASIA,  " +
            "       P.EMAIL AS EMAIL,  " +
            "       P.HOMEPAGE AS SITE,  " +
            "       COALESCE((SELECT RG.NUMERO FROM DOCUMENTOPESSOAL DP  " +
            "                                          INNER JOIN RG ON RG.ID = DP.ID  " +
            "                                          WHERE DP.PESSOAFISICA_ID = PF.ID  " +
            "                                            AND RG.NUMERO != NULL  " +
            "                                            AND ROWNUM = 1), PJ.INSCRICAOESTADUAL) AS RG_INSCRICAOESTADUAL," +
            "       EC.CEP AS CEP," +
            "       EC.LOGRADOURO AS LOGRADOURO," +
            "       EC.NUMERO AS NUMERO," +
            "       EC.COMPLEMENTO AS COMPLEMENTO," +
            "       EC.BAIRRO AS BAIRRO," +
            "       EC.LOCALIDADE AS LOCALIDADE," +
            "       EC.UF AS UF   ";
    private static final String SQL_FROM = "   FROM PESSOA P  " +
            "  LEFT JOIN PESSOAFISICA PF ON PF.ID = P.ID  " +
            "  LEFT JOIN PESSOAJURIDICA PJ ON PJ.ID = P.ID  " +
            "  LEFT JOIN ENDERECOCORREIO EC ON EC.ID = P.ENDERECOPRINCIPAL_ID ";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IdJDBCRepository idJDBCRepository;

    public PessoaNfseDTO findById(Long id) {
        List<PessoaNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                " WHERE P.ID = ? ", new Object[]{id}, new PessoaMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public Long getIdArquivo(Long idPessoa) {
        List<Long> query = jdbcTemplate.query(" SELECT ARQUIVO_ID AS VALUE FROM PESSOA P " +
                " WHERE P.ID = ? ", new Object[]{idPessoa}, new LongMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public void update(PessoaNfseDTO pessoa) {
        jdbcTemplate.batchUpdate(" update pessoa set email = ?, homepage = ? " +
                " where id = ? ", new PessoaSetter(pessoa, true));
    }

    public void updateArquivo(PessoaNfseDTO pessoa, ArquivoNfseDTO arquivo) {
        jdbcTemplate.batchUpdate(" update pessoa set arquivo_id = ? " +
                " where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, arquivo.getId());
                preparedStatement.setLong(2, pessoa.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void saveTelefones(PessoaNfseDTO pessoa) {
        List<TelefoneNfseDTO> telefones = findTelefones(pessoa.getId());
        saveTelefone(pessoa, TelefoneNfseDTO.getTelefonePorTipo("FIXO", telefones),
                pessoa.getDadosPessoais().getTelefone(),
                "FIXO");
        saveTelefone(pessoa, TelefoneNfseDTO.getTelefonePorTipo("CELULAR", telefones),
                pessoa.getDadosPessoais().getCelular(),
                "CELULAR");
    }

    private void saveTelefone(PessoaNfseDTO pessoa,
                              TelefoneNfseDTO registrado,
                              String telefone,
                              String tipoTelefone) {
        if (registrado == null) {
            registrado = new TelefoneNfseDTO();
            registrado.setTelefone(telefone);
            registrado.setTipoTelefone(tipoTelefone);
            registrado.setPrincipal(Boolean.FALSE);
            inserirTelefone(pessoa, registrado);
        } else {
            registrado.setTelefone(telefone);
            updateTelefone(pessoa, registrado);
        }
    }

    public PessoaNfseDTO findByCpfCnpj(String cpfCnpj) {
        List<PessoaNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                "WHERE COALESCE(PF.CPF, PJ.CNPJ) = ? ", new Object[]{cpfCnpj}, new PessoaMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<TelefoneNfseDTO> findTelefones(Long id) {
        return jdbcTemplate.query(" SELECT " +
                " T.ID AS ID," +
                " T.TIPOFONE AS TIPOTELEFONE," +
                " T.TELEFONE AS TELEFONE," +
                " COALESCE(T.PRINCIPAL, 0) AS PRINCIPAL " +
                " FROM TELEFONE T " +
                " WHERE T.PESSOA_ID = ? ", new Object[]{id}, new TelefoneMapper());
    }

    private void inserirTelefone(PessoaNfseDTO pessoa, TelefoneNfseDTO telefone) {
        telefone.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO TELEFONE (ID, DATAREGISTRO, TIPOFONE, TELEFONE, PESSOA_ID, PRINCIPAL) " +
                " VALUES (?, CURRENT_DATE, ?, ?, ?, ?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, telefone.getId());
                preparedStatement.setString(2, telefone.getTipoTelefone());
                preparedStatement.setString(3, telefone.getTelefone());
                preparedStatement.setLong(4, pessoa.getId());
                preparedStatement.setBoolean(5, telefone.getPrincipal());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });

        updateTelefonePrincipal(pessoa, telefone);
    }

    private void updateTelefone(PessoaNfseDTO pessoa, TelefoneNfseDTO telefone) {
        jdbcTemplate.batchUpdate(" UPDATE TELEFONE SET TIPOFONE = ?, TELEFONE = ?, PRINCIPAL = ? " +
                " WHERE ID = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, telefone.getTipoTelefone());
                preparedStatement.setString(2, telefone.getTelefone());
                preparedStatement.setBoolean(3, telefone.getPrincipal());
                preparedStatement.setLong(4, telefone.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });

        updateTelefonePrincipal(pessoa, telefone);
    }

    private void updateTelefonePrincipal(PessoaNfseDTO pessoa, TelefoneNfseDTO telefone) {
        if (!telefone.getPrincipal()) {
            return;
        }
        jdbcTemplate.batchUpdate(" UPDATE PESSOA SET TELEFONEPRINCIPAL_ID = ? WHERE ID = ? ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, telefone.getId());
                        preparedStatement.setLong(2, pessoa.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });


    }
}
