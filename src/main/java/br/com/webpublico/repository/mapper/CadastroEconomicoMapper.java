package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.EnquadramentoFiscalNfseDTO;
import br.com.webpublico.domain.dto.NaturezaJuridicaNfseDTO;
import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoCadastralNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CadastroEconomicoMapper implements RowMapper<PrestadorServicoNfseDTO> {

    @Override
    public PrestadorServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        PrestadorServicoNfseDTO dto = new PrestadorServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setInscricaoMunicipal(resultSet.getString("INSCRICAOCADASTRAL"));
        if (resultSet.getLong("PESSOA_ID") != 0) {
            dto.setPessoa(new PessoaNfseDTO());
            dto.getPessoa().setId(resultSet.getLong("PESSOA_ID"));
        }
        dto.setEnviaEmailNfseTomador(resultSet.getBoolean("ENVIAEMAILNFSETOMADOR"));
        dto.setEnviaEmailNfseContador(resultSet.getBoolean("ENVIAEMAILNFSECONTADOR"));
        dto.setEnviaEmailCancelaNfseTomador(resultSet.getBoolean("ENVIAEMAILCANCELANFSETOMADOR"));
        dto.setEnviaEmailCancelaNfseContador(resultSet.getBoolean("ENVIAEMAILCANCELANFSECONTADOR"));
        dto.setNomeParaContato(resultSet.getString("NOMEPARACONTATO"));
        dto.setTelefoneParaContato(resultSet.getString("TELEFONEPARACONTATO"));
        dto.setResumoSobreEmpresa(resultSet.getString("RESUMOSOBREEMPRESA"));
        dto.setChaveAcesso(resultSet.getString("CHAVEACESSO"));
        if (resultSet.getLong("NATUREZAJURIDICA_ID") != 0) {
            dto.setNaturezaJuridica(new NaturezaJuridicaNfseDTO());
            dto.getNaturezaJuridica().setId(resultSet.getLong("NATUREZAJURIDICA_ID"));
        }
        if (resultSet.getLong("ENQUADRAMENTO_ID") != 0) {
            dto.setEnquadramentoFiscal(new EnquadramentoFiscalNfseDTO());
            dto.getEnquadramentoFiscal().setId(resultSet.getLong("ENQUADRAMENTO_ID"));
        }
        dto.setSituacao(!Strings.isNullOrEmpty(resultSet.getString("SITUACAO")) ?
                SituacaoCadastralNfseDTO.valueOf(resultSet.getString("SITUACAO")) : null);
        dto.setAbertura(resultSet.getDate("ABERTURA"));
        dto.setEncerramento(resultSet.getDate("ENCERRAMENTO"));
        return dto;
    }
}
