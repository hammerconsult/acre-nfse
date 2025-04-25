package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.CosifNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.ServicoNfseDTO;
import br.com.webpublico.exception.OperacaoNaoPermitidaException;
import com.beust.jcommander.Strings;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class PlanoGeralContasComentadoNfseDTO extends AbstractEntity implements RowMapper<PlanoGeralContasComentadoNfseDTO>, BatchPreparedStatementSetter {

    private PrestadorServicoNfseDTO prestador;
    private Date inicioVigencia;
    private Date fimVigencia;
    private String conta;
    private String desdobramento;
    private String nome;
    private String descricaoDetalhada;
    private Long idContaSuperior;
    private String contaSuperior;
    private CosifNfseDTO cosif;
    private ServicoNfseDTO servico;
    private CodigoTributacaoNfseDTO codigoTributacao;
    private PlanoGeralContasComentadoTarifaBancariaNfseDTO tarifaBancaria;
    private PlanoGeralContasComentadoProdutoServicoNfseDTO produtoServico;

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public Date getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(Date fimVigencia) {
        this.fimVigencia = fimVigencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDesdobramento() {
        return desdobramento;
    }

    public void setDesdobramento(String desdobramento) {
        this.desdobramento = desdobramento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricaoDetalhada() {
        return descricaoDetalhada;
    }

    public void setDescricaoDetalhada(String descricaoDetalhada) {
        this.descricaoDetalhada = descricaoDetalhada;
    }

    public Long getIdContaSuperior() {
        return idContaSuperior;
    }

    public void setIdContaSuperior(Long idContaSuperior) {
        this.idContaSuperior = idContaSuperior;
    }

    public String getContaSuperior() {
        return contaSuperior;
    }

    public void setContaSuperior(String contaSuperior) {
        this.contaSuperior = contaSuperior;
    }

    public CosifNfseDTO getCosif() {
        return cosif;
    }

    public void setCosif(CosifNfseDTO cosif) {
        this.cosif = cosif;
    }

    public ServicoNfseDTO getServico() {
        return servico;
    }

    public void setServico(ServicoNfseDTO servico) {
        this.servico = servico;
    }

    public CodigoTributacaoNfseDTO getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(CodigoTributacaoNfseDTO codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    public PlanoGeralContasComentadoTarifaBancariaNfseDTO getTarifaBancaria() {
        return tarifaBancaria;
    }

    public void setTarifaBancaria(PlanoGeralContasComentadoTarifaBancariaNfseDTO tarifaBancaria) {
        this.tarifaBancaria = tarifaBancaria;
    }

    public PlanoGeralContasComentadoProdutoServicoNfseDTO getProdutoServico() {
        return produtoServico;
    }

    public void setProdutoServico(PlanoGeralContasComentadoProdutoServicoNfseDTO produtoServico) {
        this.produtoServico = produtoServico;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    @Override
    public PlanoGeralContasComentadoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        PlanoGeralContasComentadoNfseDTO dto = new PlanoGeralContasComentadoNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("cadastroeconomico_id"));
        dto.setConta(resultSet.getString("conta"));
        dto.setDesdobramento(resultSet.getString("desdobramento"));
        dto.setNome(resultSet.getString("nome"));
        dto.setDescricaoDetalhada(resultSet.getString("descricaodetalhada"));
        dto.setInicioVigencia(resultSet.getDate("iniciovigencia"));
        dto.setFimVigencia(resultSet.getDate("fimvigencia"));
        if (resultSet.getLong("id_superior") != 0) {
            dto.setIdContaSuperior(resultSet.getLong("id_superior"));
            dto.setContaSuperior(resultSet.getString("conta_superior"));
        }
        if (resultSet.getLong("cosif_id") != 0) {
            dto.setCosif(new CosifNfseDTO());
            dto.getCosif().setId(resultSet.getLong("cosif_id"));
        }
        if (resultSet.getLong("servico_id") != 0) {
            dto.setServico(new ServicoNfseDTO());
            dto.getServico().setId(resultSet.getLong("servico_id"));
        }
        if (resultSet.getLong("codigotributacao_id") != 0) {
            dto.setCodigoTributacao(new CodigoTributacaoNfseDTO());
            dto.getCodigoTributacao().setId(resultSet.getLong("codigotributacao_id"));
        }
        if (resultSet.getLong("id_rel_tarifa") != 0) {
            dto.setTarifaBancaria(new PlanoGeralContasComentadoTarifaBancariaNfseDTO());
            dto.getTarifaBancaria().setId(resultSet.getLong("id_rel_tarifa"));
            dto.getTarifaBancaria().setTarifaBancaria(new TarifaBancariaNfseDTO());
            dto.getTarifaBancaria().getTarifaBancaria().setId(resultSet.getLong("id_tarifa"));
            dto.getTarifaBancaria().setInicioVigencia(resultSet.getDate("iniciovigencia_tarifa"));
            dto.getTarifaBancaria().setValorUnitario(resultSet.getBigDecimal("valorunitario_tarifa"));
            dto.getTarifaBancaria().setValorPercentual(resultSet.getBigDecimal("valorpercentual_tarifa"));
        }
        if (resultSet.getLong("id_rel_prodserv") != 0) {
            dto.setProdutoServico(new PlanoGeralContasComentadoProdutoServicoNfseDTO());
            dto.getProdutoServico().setId(resultSet.getLong("id_rel_prodserv"));
            dto.getProdutoServico().setProdutoServico(new ProdutoServicoBancarioNfseDTO());
            dto.getProdutoServico().getProdutoServico().setId(resultSet.getLong("id_prodserv"));
            dto.getProdutoServico().setDescricaoComplementar(resultSet.getString("descricaocomplementar_prodserv"));
        }
        return dto;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        if (!update) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, prestador.getId());
            preparedStatement.setDate(3, DateUtils.toSQLDate(inicioVigencia));
            preparedStatement.setDate(4, DateUtils.toSQLDate(fimVigencia));
            preparedStatement.setString(5, conta);
            preparedStatement.setString(6, desdobramento);
            preparedStatement.setString(7, nome);
            preparedStatement.setString(8, descricaoDetalhada);
            if (cosif != null) {
                preparedStatement.setLong(9, cosif.getId());
            } else {
                preparedStatement.setNull(9, Types.NULL);
            }
            if (servico != null) {
                preparedStatement.setLong(10, servico.getId());
            } else {
                preparedStatement.setNull(10, Types.NULL);
            }
            if (codigoTributacao != null) {
                preparedStatement.setLong(11, codigoTributacao.getId());
            } else {
                preparedStatement.setNull(11, Types.NULL);
            }
            if (tarifaBancaria != null) {
                preparedStatement.setLong(12, tarifaBancaria.getId());
            } else {
                preparedStatement.setNull(12, Types.NULL);
            }
            if (produtoServico != null) {
                preparedStatement.setLong(13, produtoServico.getId());
            } else {
                preparedStatement.setNull(13, Types.NULL);
            }
            if (idContaSuperior != null) {
                preparedStatement.setLong(14, idContaSuperior);
            } else {
                preparedStatement.setNull(14, Types.NULL);
            }
        } else {
            preparedStatement.setDate(1, DateUtils.toSQLDate(inicioVigencia));
            preparedStatement.setDate(2, DateUtils.toSQLDate(fimVigencia));
            preparedStatement.setString(3, conta);
            preparedStatement.setString(4, desdobramento);
            preparedStatement.setString(5, nome);
            preparedStatement.setString(6, descricaoDetalhada);
            if (cosif != null) {
                preparedStatement.setLong(7, cosif.getId());
            } else {
                preparedStatement.setNull(7, Types.NULL);
            }
            if (servico != null) {
                preparedStatement.setLong(8, servico.getId());
            } else {
                preparedStatement.setNull(8, Types.NULL);
            }
            if (codigoTributacao != null) {
                preparedStatement.setLong(9, codigoTributacao.getId());
            } else {
                preparedStatement.setNull(9, Types.NULL);
            }
            if (tarifaBancaria != null) {
                preparedStatement.setLong(10, tarifaBancaria.getId());
            } else {
                preparedStatement.setNull(10, Types.NULL);
            }
            if (produtoServico != null) {
                preparedStatement.setLong(11, produtoServico.getId());
            } else {
                preparedStatement.setNull(11, Types.NULL);
            }
            if (idContaSuperior != null) {
                preparedStatement.setLong(12, idContaSuperior);
            } else {
                preparedStatement.setNull(12, Types.NULL);
            }
            preparedStatement.setLong(13, id);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public void realizarValidacoes() throws OperacaoNaoPermitidaException {
        OperacaoNaoPermitidaException onpe = new OperacaoNaoPermitidaException();
        if (inicioVigencia == null) {
            onpe.adicionarMensagem("O campo Início de Vigência deve ser informado.");
        }
        if (Strings.isStringEmpty(conta)) {
            onpe.adicionarMensagem("O campo Conta deve ser informado.");
        }
        onpe.lancarExcecao();
    }
}
