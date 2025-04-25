package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.PaisNfseDTO;
import br.com.webpublico.domain.dto.enums.RegimeTributarioNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoIssqnNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DadosPessoaisMapper implements RowMapper<DadosPessoaisNfseDTO> {

    @Override
    public DadosPessoaisNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        DadosPessoaisNfseDTO dto = new DadosPessoaisNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCpfCnpj(resultSet.getString("CPFCNPJ"));
        dto.setInscricaoMunicipal(resultSet.getString("INSCRICAOMUNICIPAL"));
        dto.setInscricaoEstadualRg(resultSet.getString("INSCRICAOESTADUALRG"));
        dto.setNomeRazaoSocial(resultSet.getString("NOMERAZAOSOCIAL"));
        dto.setNomeFantasia(resultSet.getString("NOMEFANTASIA"));
        dto.setApelido(resultSet.getString("APELIDO"));
        dto.setEmail(resultSet.getString("EMAIL"));
        dto.setTelefone(resultSet.getString("TELEFONE"));
        dto.setCelular(resultSet.getString("CELULAR"));
        dto.setCep(resultSet.getString("CEP"));
        dto.setNumero(resultSet.getString("NUMERO"));
        dto.setLogradouro(resultSet.getString("LOGRADOURO"));
        dto.setBairro(resultSet.getString("BAIRRO"));
        dto.setComplemento(resultSet.getString("COMPLEMENTO"));
        dto.setMunicipio(new MunicipioNfseDTO());
        dto.getMunicipio().setCodigoIbge(resultSet.getString("CODIGOMUNICIPIO"));
        dto.getMunicipio().setNome(resultSet.getString("MUNICIPIO"));
        dto.getMunicipio().setEstado(resultSet.getString("UF"));
        dto.setPais(new PaisNfseDTO());
        dto.getPais().setCodigo(resultSet.getString("CODIGOPAIS"));
        dto.getPais().setNome(resultSet.getString("PAIS"));
        dto.setNumeroIdentificacao(resultSet.getString("NUMEROIDENTIFICACAO"));
        if (!Strings.isNullOrEmpty(resultSet.getString("TIPOISSQN")))
            dto.setTipoIssqn(TipoIssqnNfseDTO.valueOf(resultSet.getString("TIPOISSQN")));
        if (!Strings.isNullOrEmpty(resultSet.getString("REGIMETRIBUTARIO")))
            dto.setRegimeTributario(RegimeTributarioNfseDTO.valueOf(resultSet.getString("REGIMETRIBUTARIO")));
        return dto;
    }
}
