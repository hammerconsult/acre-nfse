package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConfiguracaoDividaNfseDTO;
import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.ConfiguracaoNfseParametroNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.repository.mapper.ConfiguracaoMapper;
import br.com.webpublico.repository.mapper.ConfiguracaoParametroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ConfiguracaoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ConfiguracaoNfseDTO find() {
        List<ConfiguracaoNfseDTO> query = jdbcTemplate.query(" SELECT C.ID AS ID, C.CIDADE_ID AS CIDADE_ID," +
                        " C.PADRAOSERVICOPRESTADO_ID AS PADRAOSERVICOPRESTADO_ID, " +
                        " CR.SECRETARIA AS SECRETARIA, CR.DEPARTAMENTO AS DEPARTAMENTO, CR.ENDERECO AS ENDERECO,  " +
                        " CR.ARQUIVOBRASAO_ID AS ARQUIVOBRASAO_ID," +
                        " CD.TIPOMANUAL_ID AS TIPOMANUAL_DESIF " +
                        "  FROM CONFIGURACAONFSE C " +
                        " LEFT JOIN CONFIGURACAONFSERELATORIO CR ON CR.ID = C.CONFIGURACAONFSERELATORIO_ID" +
                        " LEFT JOIN CONFIGURACAODESIF CD ON CD.ID = C.CONFIGURACAODESIF_ID ",
                new ConfiguracaoMapper());
        if (query != null && !query.isEmpty()) {
            return query.get(0);
        }
        return null;
    }

    public List<ConfiguracaoNfseParametroNfseDTO> findParametros(Long idConfiguracao) {
        return jdbcTemplate.query(" SELECT ID, TIPOPARAMETRO, VALOR " +
                        " FROM CONFIGURACAONFSEPARAMETROS where CONFIGURACAO_ID = ?",
                new Object[]{idConfiguracao}, new ConfiguracaoParametroMapper());
    }

    public ConfiguracaoDividaNfseDTO getConfiguracaoDivida(ConfiguracaoNfseDTO configuracao,
                                                           TipoMovimentoMensalNfseDTO tipoMovimento,
                                                           TipoDeclaracaoMensalServicoNfseDTO tipoDeclaracaoMensalServico) {
        List<ConfiguracaoDividaNfseDTO> query = jdbcTemplate.query(" select dividanfse_id, tributo_id " +
                        "   from configuracaonfsedivida " +
                        "where configuracaonfse_id = ? " +
                        "  and tipomovimentomensal = ? " +
                        "  and tipodeclaracaomensalservico = ? ",
                new Object[]{configuracao.getId(), tipoMovimento.name(), tipoDeclaracaoMensalServico.name()},
                new ConfiguracaoDividaNfseDTO());
        if (query != null && query.stream().findFirst().isPresent())
            return query.stream().findFirst().get();
        return null;
    }
}
