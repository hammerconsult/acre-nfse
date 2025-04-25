package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.CosifNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArquivoDesifRegistro0100NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0100NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private String conta;
    private String desdobramento;
    private String nome;
    private String descricao;
    private String contaSuperior;
    private CosifNfseDTO cosif;
    private CodigoTributacaoNfseDTO codigoTributacao;

    public Long getLinha() {
        return linha;
    }

    public void setLinha(Long linha) {
        this.linha = linha;
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public CodigoTributacaoNfseDTO getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(CodigoTributacaoNfseDTO codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setString(4, conta);
        preparedStatement.setString(5, desdobramento);
        preparedStatement.setString(6, nome);
        preparedStatement.setString(7, descricao);
        preparedStatement.setString(8, contaSuperior);
        preparedStatement.setLong(9, cosif.getId());
        preparedStatement.setLong(10, codigoTributacao.getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0100NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0100NfseDTO dto = new ArquivoDesifRegistro0100NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setId(resultSet.getLong("arquivodesif_id"));
        dto.setConta(resultSet.getString("conta"));
        dto.setDesdobramento(resultSet.getString("desdobramento"));
        dto.setNome(resultSet.getString("nome"));
        dto.setDescricao(resultSet.getString("descricao"));
        dto.setContaSuperior(resultSet.getString("contasuperior"));
        dto.setCosif(new CosifNfseDTO());
        dto.getCosif().setId(resultSet.getLong("cosif_id"));
        dto.setCodigoTributacao(new CodigoTributacaoNfseDTO());
        dto.getCodigoTributacao().setId(resultSet.getLong("codigotributacao_id"));
        return dto;
    }
}
