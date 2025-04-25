package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoPessoaNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PessoaMapper implements RowMapper<PessoaNfseDTO> {
    @Override
    public PessoaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        PessoaNfseDTO dto = new PessoaNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setTipoPessoa(TipoPessoaNfseDTO.valueOf(resultSet.getString("TIPO_PESSOA")));
        dto.setDadosPessoais(new DadosPessoaisNfseDTO());
        dto.getDadosPessoais().setCpfCnpj(resultSet.getString("CPF_CNPJ"));
        dto.getDadosPessoais().setNomeRazaoSocial(resultSet.getString("NOME_RAZAOSOCIAL"));
        dto.getDadosPessoais().setNomeFantasia(resultSet.getString("NOME_FANTASIA"));
        dto.getDadosPessoais().setEmail(resultSet.getString("EMAIL"));
        dto.getDadosPessoais().setSite(resultSet.getString("SITE"));
        dto.getDadosPessoais().setInscricaoEstadualRg(resultSet.getString("RG_INSCRICAOESTADUAL"));
        dto.getDadosPessoais().setCep(resultSet.getString("CEP"));
        dto.getDadosPessoais().setLogradouro(resultSet.getString("LOGRADOURO"));
        dto.getDadosPessoais().setNumero(resultSet.getString("NUMERO"));
        dto.getDadosPessoais().setComplemento(resultSet.getString("COMPLEMENTO"));
        dto.getDadosPessoais().setBairro(resultSet.getString("BAIRRO"));
        if (!Strings.isNullOrEmpty(resultSet.getString("LOCALIDADE"))
                && !Strings.isNullOrEmpty(resultSet.getString("UF"))) {
            dto.getDadosPessoais().setMunicipio(new MunicipioNfseDTO(resultSet.getString("LOCALIDADE"),
                    resultSet.getString("UF")));
        }
        return dto;
    }
}
