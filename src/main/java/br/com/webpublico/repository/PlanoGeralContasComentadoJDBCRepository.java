package br.com.webpublico.repository;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoProdutoServicoNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoTarifaBancariaNfseDTO;
import com.beust.jcommander.Strings;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository
public class PlanoGeralContasComentadoJDBCRepository extends AbstractJDBCRepository<PlanoGeralContasComentadoNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, obj.cadastroeconomico_id, obj.iniciovigencia, " +
                " obj.fimvigencia, obj.conta, obj.desdobramento, obj.nome, obj.descricaodetalhada, " +
                " obj.cosif_id, obj.servico_id, obj.codigotributacao_id, " +
                " pgccs.id as id_superior, pgccs.conta as conta_superior, " +
                " pgcct.id as id_rel_tarifa, pgcct.tarifabancaria_id as id_tarifa, pgcct.iniciovigencia as iniciovigencia_tarifa," +
                " pgcct.valorunitario as valorunitario_tarifa, pgcct.valorpercentual as valorpercentual_tarifa," +
                " pgccps.id as id_rel_prodserv, pgccps.produtoservicobancario_id as id_prodserv, " +
                " pgccps.descricaocomplementar as descricaocomplementar_prodserv ";
    }

    @Override
    public String getFrom() {
        return " from pgcc obj " +
                " left join pgcc pgccs on pgccs.id = obj.contasuperior_id " +
                " left join pgcctarifabancaria pgcct on pgcct.id = obj.tarifabancaria_id" +
                " left join pgccprodutoservico pgccps on pgccps.id = obj.produtoservico_id ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.conta asc ";
    }

    @Override
    public PlanoGeralContasComentadoNfseDTO newRowMapper() {
        return new PlanoGeralContasComentadoNfseDTO();
    }

    public PlanoGeralContasComentadoNfseDTO findByContaAndDesdobramentoAndReferencia(Long prestador,
                                                                                     String conta,
                                                                                     String desdobramento,
                                                                                     Date referencia) {
        String sql = getSelect() +
                getFrom() +
                " where obj.cadastroeconomico_id = :cadastro " +
                "   and obj.conta = :conta " +
                "   and to_date(:referencia, 'dd/MM/yyyy') between trunc(obj.iniciovigencia) " +
                "   and trunc(coalesce(obj.fimvigencia, to_date(:referencia, 'dd/MM/yyyy'))) ";
        if (!Strings.isStringEmpty(desdobramento)) {
            sql += " and obj.desdobramento = :desdobramento ";
        }

        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("cadastro", prestador);
        parametros.addValue("conta", conta);
        if (!Strings.isStringEmpty(desdobramento)) {
            parametros.addValue("desdobramento", desdobramento);
        }
        parametros.addValue("referencia", DateUtils.getDataFormatada(referencia));

        List<PlanoGeralContasComentadoNfseDTO> query = namedParameterJdbcTemplate.query(sql,
                parametros,
                newRowMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    @Override
    public PlanoGeralContasComentadoNfseDTO insert(PlanoGeralContasComentadoNfseDTO dto) {
        savePgccTarifaBancaria(dto.getTarifaBancaria());
        savePgccProdutoServico(dto.getProdutoServico());
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("insert into pgcc (id, cadastroeconomico_id, iniciovigencia, fimvigencia, conta, desdobramento, nome, " +
                " descricaodetalhada, cosif_id, servico_id, codigotributacao_id, tarifabancaria_id, " +
                " produtoservico_id, contasuperior_id) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public PlanoGeralContasComentadoNfseDTO update(PlanoGeralContasComentadoNfseDTO dto) {
        savePgccTarifaBancaria(dto.getTarifaBancaria());
        savePgccProdutoServico(dto.getProdutoServico());
        dto.setUpdate(Boolean.TRUE);
        jdbcTemplate.batchUpdate(" update pgcc set iniciovigencia = ?, fimvigencia = ?, conta = ?, desdobramento = ?, nome = ?, " +
                " descricaodetalhada = ?, cosif_id = ?, servico_id = ?, codigotributacao_id = ?, tarifabancaria_id = ?, " +
                " produtoservico_id = ?, contasuperior_id = ? " +
                " where id = ? ", dto);
        return dto;
    }

    @Override
    public void remove(PlanoGeralContasComentadoNfseDTO dto) {
        jdbcTemplate.batchUpdate(" delete from pgcc where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, dto.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void savePgccTarifaBancaria(PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        if (pgccTarifaBancaria != null) {
            if (pgccTarifaBancaria.getId() == null) {
                insertPgccTarifaBancaria(pgccTarifaBancaria);
            } else {
                updatePgccTarifaBancaria(pgccTarifaBancaria);
            }
        }
    }

    private void insertPgccTarifaBancaria(PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        pgccTarifaBancaria.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into pgcctarifabancaria (id, tarifabancaria_id, iniciovigencia, valorunitario, valorpercentual) " +
                " values (?, ?, ?, ?, ?) ", pgccTarifaBancaria);
    }

    private void updatePgccTarifaBancaria(PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        pgccTarifaBancaria.setUpdate(true);
        jdbcTemplate.batchUpdate(" update pgcctarifabancaria set tarifabancaria_id = ?, iniciovigencia = ?, valorunitario = ?, valorpercentual = ? " +
                " where id = ? ", pgccTarifaBancaria);
    }

    public void removePgccTarifaBancaria(PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        jdbcTemplate.batchUpdate(" delete from pgcctarifabancaria where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, pgccTarifaBancaria.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void changePgccTarifaBancaria(PlanoGeralContasComentadoNfseDTO pgcc,
                                         PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        jdbcTemplate.batchUpdate(" update pgcc set tarifabancaria_id = ? where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                if (pgccTarifaBancaria != null) {
                    preparedStatement.setLong(1, pgccTarifaBancaria.getId());
                } else {
                    preparedStatement.setNull(1, Types.NULL);
                }
                preparedStatement.setLong(2, pgcc.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void savePgccProdutoServico(PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        if (pgccProdutoServico != null) {
            if (pgccProdutoServico.getId() == null) {
                insertPgccProdutoServico(pgccProdutoServico);
            } else {
                updatePgccProdutoServico(pgccProdutoServico);
            }
        }
    }

    private void insertPgccProdutoServico(PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        pgccProdutoServico.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into pgccprodutoservico (id, produtoservicobancario_id, descricaocomplementar) " +
                " values (?, ?, ?) ", pgccProdutoServico);
    }

    private void updatePgccProdutoServico(PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        pgccProdutoServico.setUpdate(true);
        jdbcTemplate.batchUpdate(" update pgccprodutoservico set produtoservicobancario_id = ?, descricaocomplementar = ? " +
                " where id = ? ", pgccProdutoServico);
    }

    public void removePgccProdutoServico(PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        jdbcTemplate.batchUpdate(" delete from pgccprodutoservico where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, pgccProdutoServico.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void changePgccProdutoServico(PlanoGeralContasComentadoNfseDTO pgcc,
                                         PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        jdbcTemplate.batchUpdate(" update pgcc set produtoservico_id = ? where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                if (pgccProdutoServico != null) {
                    preparedStatement.setLong(1, pgccProdutoServico.getId());
                } else {
                    preparedStatement.setNull(1, Types.NULL);
                }
                preparedStatement.setLong(2, pgcc.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public List<PlanoGeralContasComentadoNfseDTO> buscarContasTributaveisNaoDeclaradas(Long prestador,
                                                                                       Long arquivo) {
        String sql = getSelect() +
                getFrom() +
                " where obj.cadastroeconomico_id = :cadastro " +
                "   and to_date(:referencia, 'dd/MM/yyyy') between trunc(obj.iniciovigencia) " +
                "   and trunc(coalesce(obj.fimvigencia, to_date(:referencia, 'dd/MM/yyyy'))) " +
                "   and obj.contatributavel = :tributavel " +
                "   and not exists (select 1 " +
                "                      from arquivodesifregistro0430 reg " +
                "                   where reg.arquivodesif_id = :arquivo " +
                "                     and reg.conta_id = obj.id) " +
                " order by obj.conta ";
        ;

        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("cadastro", prestador);
        parametros.addValue("referencia", DateUtils.getDataFormatada(new Date()));
        parametros.addValue("tributavel", 1);
        parametros.addValue("arquivo", arquivo);

        return namedParameterJdbcTemplate.query(sql,
                parametros,
                newRowMapper());
    }
}
