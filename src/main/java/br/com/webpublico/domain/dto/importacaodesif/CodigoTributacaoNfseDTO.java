package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.ServicoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CodigoTributacaoNfseDTO extends AbstractEntity implements RowMapper<CodigoTributacaoNfseDTO> {

    private String codigo;
    private String descricao;
    private ServicoNfseDTO servico;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ServicoNfseDTO getServico() {
        return servico;
    }

    public void setServico(ServicoNfseDTO servico) {
        this.servico = servico;
    }

    @Override
    public CodigoTributacaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        CodigoTributacaoNfseDTO dto = new CodigoTributacaoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCodigo(resultSet.getString("CODIGO"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        dto.setServico(new ServicoNfseDTO());
        dto.getServico().setId(resultSet.getLong("SERVICO_ID"));
        return dto;
    }
}
