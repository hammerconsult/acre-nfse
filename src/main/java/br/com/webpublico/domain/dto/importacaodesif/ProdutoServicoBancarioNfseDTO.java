package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoServicoBancarioNfseDTO extends AbstractEntity implements RowMapper<ProdutoServicoBancarioNfseDTO> {

    private GrupoProdutoServicoBancarioNfseDTO grupo;
    private Integer codigo;
    private String descricao;
    private Boolean obrigaDescricaoComplementar;

    public GrupoProdutoServicoBancarioNfseDTO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoProdutoServicoBancarioNfseDTO grupo) {
        this.grupo = grupo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getObrigaDescricaoComplementar() {
        return obrigaDescricaoComplementar;
    }

    public void setObrigaDescricaoComplementar(Boolean obrigaDescricaoComplementar) {
        this.obrigaDescricaoComplementar = obrigaDescricaoComplementar;
    }

    @Override
    public ProdutoServicoBancarioNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ProdutoServicoBancarioNfseDTO dto = new ProdutoServicoBancarioNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCodigo(resultSet.getInt("CODIGO"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        dto.setGrupo(GrupoProdutoServicoBancarioNfseDTO.valueOf(resultSet.getString("GRUPO")));
        dto.setObrigaDescricaoComplementar(resultSet.getBoolean("OBRIGADESCRICAOCOMPLEMENTAR"));
        return dto;
    }
}
