package br.com.webpublico.repository;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.DebitoItemNfseDTO;
import br.com.webpublico.domain.dto.DebitoNfseDTO;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.LongMapper;
import br.com.webpublico.tributario.consultadebitos.enums.SituacaoParcelaDTO;
import br.com.webpublico.tributario.consultadebitos.enums.TipoCalculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository
public class DebitoJDBCRepository implements Serializable {

    private final JdbcTemplate jdbcTemplate;
    private final IdJDBCRepository idJDBCRepository;

    public DebitoJDBCRepository(JdbcTemplate jdbcTemplate, IdJDBCRepository idJDBCRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.idJDBCRepository = idJDBCRepository;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private void inserirProcesso(DebitoNfseDTO debito) {
        debito.setIdProcesso(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("INSERT INTO PROCESSOCALCULO " +
                "(ID, EXERCICIO_ID, DIVIDA_ID, DATALANCAMENTO, DESCRICAO) " +
                "VALUES (?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, debito.getIdProcesso());
                ps.setLong(2, debito.getIdExercicio());
                ps.setLong(3, debito.getIdDivida());
                ps.setDate(4, DateUtils.toSQLDate(debito.getDataLancamento()));
                ps.setString(5, debito.getDescricao());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
        jdbcTemplate.batchUpdate(" INSERT INTO PROCESSOCALCULOISS " +
                " (ID, MESREFERENCIA) " +
                " VALUES (?,?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, debito.getIdProcesso());
                ps.setLong(2, debito.getMesReferencia());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    private void inserirCalculo(DebitoNfseDTO debito) {
        debito.setIdCalculo(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("INSERT INTO CALCULO " +
                        " (ID, DATACALCULO, SIMULACAO, VALORREAL, VALOREFETIVO, " +
                        " ISENTO, CADASTRO_ID, SUBDIVIDA, TIPOCALCULO, CONSISTENTE, " +
                        " PROCESSOCALCULO_ID) " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdCalculo());
                        ps.setDate(2, DateUtils.toSQLDate(debito.getDataLancamento()));
                        ps.setBoolean(3, Boolean.FALSE);
                        ps.setBigDecimal(4, debito.getValorCalculado());
                        ps.setBigDecimal(5, debito.getValorCalculado());
                        ps.setBoolean(6, Boolean.FALSE);
                        if (debito.getIdCadastro() != null) {
                            ps.setLong(7, debito.getIdCadastro());
                        } else {
                            ps.setNull(7, Types.NULL);
                        }
                        ps.setLong(8, debito.getSubDivida());
                        ps.setString(9, TipoCalculoDTO.ISS.name());
                        ps.setBoolean(10, Boolean.TRUE);
                        ps.setLong(11, debito.getIdProcesso());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });

        jdbcTemplate.batchUpdate(" INSERT INTO CALCULOISS " +
                        " (ID, PROCESSOCALCULOISS_ID, CADASTROECONOMICO_ID, TIPOCALCULOISS, " +
                        " ALIQUOTA, BASECALCULO, VALORCALCULADO, FATURAMENTO, SEQUENCIALANCAMENTO, TAXASOBREISS, " +
                        " AUSENCIAMOVIMENTO, TIPOSITUACAOCALCULOISS) " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdCalculo());
                        ps.setLong(2, debito.getIdProcesso());
                        if (debito.getIdCadastro() != null) {
                            ps.setLong(3, debito.getIdCadastro());
                        } else {
                            ps.setNull(3, Types.NULL);
                        }
                        ps.setString(4, "MENSAL");
                        ps.setBigDecimal(5, debito.getAliquota());
                        ps.setBigDecimal(6, debito.getBaseCalculo());
                        ps.setBigDecimal(7, debito.getValorCalculado());
                        ps.setBigDecimal(8, debito.getFaturamento());
                        ps.setLong(9, debito.getSequenciaLancamento());
                        ps.setBigDecimal(10, BigDecimal.ZERO);
                        ps.setBoolean(11, debito.getAusenciaMovimento());
                        ps.setString(12, "LANCADO");
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    private void inserirCalculoPessoa(DebitoNfseDTO debito) {
        jdbcTemplate.batchUpdate(" INSERT INTO CALCULOPESSOA " +
                        " (ID, CALCULO_ID, PESSOA_ID) " +
                        " VALUES (?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, idJDBCRepository.getId());
                        ps.setLong(2, debito.getIdCalculo());
                        ps.setLong(3, debito.getIdPessoa());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    private void inserirItemCalculo(DebitoNfseDTO debito) {
        for (DebitoItemNfseDTO item : debito.getItens()) {
            item.setId(idJDBCRepository.getId());
            jdbcTemplate.batchUpdate(" INSERT INTO ITEMCALCULOISS " +
                            " (ID, SERVICO_ID, CALCULO_ID, ALIQUOTA, BASECALCULO, FATURAMENTO, TRIBUTO_ID, VALORCALCULADO) " +
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, item.getId());
                            ps.setLong(2, item.getIdServico());
                            ps.setLong(3, debito.getIdCalculo());
                            ps.setBigDecimal(4, item.getAliquota());
                            ps.setBigDecimal(5, item.getBaseCalculo());
                            ps.setBigDecimal(6, item.getValorServico());
                            ps.setLong(7, debito.getIdTributo());
                            ps.setBigDecimal(8, item.getIss());
                        }

                        @Override
                        public int getBatchSize() {
                            return 1;
                        }
                    });
        }
    }

    private void inserirValorDivida(DebitoNfseDTO debito) {
        debito.setIdValorDivida(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO VALORDIVIDA " +
                        " (ID, EMISSAO, VALOR, DIVIDA_ID, EXERCICIO_ID, CALCULO_ID) " +
                        " VALUES(?, ?, ?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdValorDivida());
                        ps.setDate(2, DateUtils.toSQLDate(debito.getDataLancamento()));
                        ps.setBigDecimal(3, debito.getValorCalculado());
                        ps.setLong(4, debito.getIdDivida());
                        ps.setLong(5, debito.getIdExercicio());
                        ps.setLong(6, debito.getIdCalculo());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    private void inserirParcelaValorDivida(DebitoNfseDTO debito) {
        debito.setIdParcelaValorDivida(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO PARCELAVALORDIVIDA " +
                        " (ID, OPCAOPAGAMENTO_ID, VALORDIVIDA_ID, VENCIMENTO, PERCENTUALVALORTOTAL, " +
                        " DATAREGISTRO, SEQUENCIAPARCELA, DIVIDAATIVA, VALOR) " +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdParcelaValorDivida());
                        ps.setLong(2, debito.getIdOpcaoPagamento());
                        ps.setLong(3, debito.getIdValorDivida());
                        ps.setDate(4, DateUtils.toSQLDate(debito.getDataVencimento()));
                        ps.setBigDecimal(5, new BigDecimal("100"));
                        ps.setDate(6, DateUtils.toSQLDate(debito.getDataLancamento()));
                        ps.setString(7, "1");
                        ps.setBoolean(8, Boolean.FALSE);
                        ps.setBigDecimal(9, debito.getValorCalculado());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    private void inserirItemParcelaValorDivida(DebitoNfseDTO debito) {
        debito.setIdItemParcelaValorDivida(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO ITEMPARCELAVALORDIVIDA " +
                        " (ID, PARCELAVALORDIVIDA_ID, ITEMVALORDIVIDA_ID, VALOR) " +
                        " VALUES(?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdItemParcelaValorDivida());
                        ps.setLong(2, debito.getIdParcelaValorDivida());
                        ps.setLong(3, debito.getIdItemValorDivida());
                        ps.setBigDecimal(4, debito.getValorCalculado());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    public void inserirSituacaoParcelaValorDivida(Date dataLancamento,
                                                  SituacaoParcelaDTO situacaoParcela,
                                                  Long idParcela,
                                                  BigDecimal saldo,
                                                  String referencia) {
        jdbcTemplate.batchUpdate(" insert into situacaoparcelavalordivida (id, datalancamento, situacaoparcela, " +
                        "                                        parcela_id, saldo, inconsistente, referencia) " +
                        " values (?, ?, ?, ?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, idJDBCRepository.getId());
                        ps.setDate(2, DateUtils.toSQLDate(dataLancamento));
                        ps.setString(3, situacaoParcela.name());
                        ps.setLong(4, idParcela);
                        ps.setBigDecimal(5, saldo);
                        ps.setBoolean(6, Boolean.FALSE);
                        ps.setString(7, referencia);
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    private void inserirSituacaoParcelaValorDivida(DebitoNfseDTO debito) {
        debito.setIdSituacaoParcelaValorDivida(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into situacaoparcelavalordivida (id, datalancamento, situacaoparcela, " +
                        "                                        parcela_id, saldo, inconsistente, referencia) " +
                        " values (?, ?, ?, ?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdSituacaoParcelaValorDivida());
                        ps.setDate(2, DateUtils.toSQLDate(debito.getDataLancamento()));
                        ps.setString(3, SituacaoParcelaDTO.EM_ABERTO.name());
                        ps.setLong(4, debito.getIdParcelaValorDivida());
                        ps.setBigDecimal(5, debito.getValorCalculado());
                        ps.setBoolean(6, Boolean.FALSE);
                        ps.setString(7, debito.getReferencia());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    private void inserirItemValorDivida(DebitoNfseDTO debito) {
        debito.setIdItemValorDivida(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO ITEMVALORDIVIDA " +
                        " (ID, VALOR, TRIBUTO_ID, VALORDIVIDA_ID) " +
                        " VALUES(?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, debito.getIdItemValorDivida());
                        ps.setBigDecimal(2, debito.getValorCalculado());
                        ps.setLong(3, debito.getIdTributo());
                        ps.setLong(4, debito.getIdValorDivida());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    public DebitoNfseDTO gravarDebito(DebitoNfseDTO debito) {
        inserirProcesso(debito);
        inserirCalculo(debito);
        inserirItemCalculo(debito);
        inserirCalculoPessoa(debito);
        if (debito.getValorCalculado().compareTo(BigDecimal.ZERO) > 0) {
            inserirValorDivida(debito);
            inserirItemValorDivida(debito);
            inserirParcelaValorDivida(debito);
            inserirItemParcelaValorDivida(debito);
            inserirSituacaoParcelaValorDivida(debito);
        }
        return debito;
    }

    public boolean hasDebitoPago(Long idCalculo) {
        List<Integer> query = jdbcTemplate.query(" select 1 as value " +
                        "   from calculo c " +
                        "  inner join valordivida vd on vd.calculo_id = c.id " +
                        "  inner join parcelavalordivida pvd on pvd.valordivida_id = vd.id " +
                        "  inner join situacaoparcelavalordivida spvd on spvd.id = pvd.situacaoatual_id " +
                        " where c.id = ? " +
                        "   and spvd.situacaoparcela = ? ",
                new Object[]{idCalculo, SituacaoParcelaDTO.PAGO.name()},
                new IntegerMapper());
        return query != null && query.stream().findFirst().isPresent();
    }

    public List<Long> buscarIdsParcelas(Long idCalculo) {
        return jdbcTemplate.query(" select pvd.id as value " +
                        "   from calculo c " +
                        "  inner join valordivida vd on vd.calculo_id = c.id " +
                        "  inner join parcelavalordivida pvd on pvd.valordivida_id = vd.id " +
                        " where c.id = ? ",
                new Object[]{idCalculo},
                new LongMapper());
    }
}
