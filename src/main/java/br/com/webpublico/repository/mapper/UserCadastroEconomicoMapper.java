package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.util.Util;
import com.google.common.base.Strings;
import org.apache.commons.compress.utils.Lists;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserCadastroEconomicoMapper implements RowMapper<PrestadorUsuarioNfseDTO> {
    @Override
    public PrestadorUsuarioNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        PrestadorUsuarioNfseDTO dto = new PrestadorUsuarioNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setIdUsuario(resultSet.getLong("USUARIOWEB_ID"));
        dto.setContador(resultSet.getBoolean("CONTADOR"));
        dto.setPermitido(resultSet.getBoolean("PERMITIDO"));
        dto.setFuncao(resultSet.getString("FUNCAO"));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("CADASTROECONOMICO_ID"));
        dto.setEmail(resultSet.getString("EMAIL"));
        dto.setNome(resultSet.getString("NOME"));
        dto.setLogin(resultSet.getString("CPF"));
        dto.setBloqueadoEmissaoNfse(resultSet.getBoolean("BLOQUEADOEMISSAONFSE"));
        dto.setTelefoneDesbloqueio(resultSet.getString("TELEFONEDESBLOQUEIO"));
        dto.setRoles(new ArrayList<>());
        String roles = resultSet.getString("ROLES");
        if (!Strings.isNullOrEmpty(roles)) {
            dto.setRoles(Arrays.asList(roles.split(",")));
        }
        dto.setPasswordTemporary(resultSet.getBoolean("PASSWORDTEMPORARY"));
        return dto;
    }
}
