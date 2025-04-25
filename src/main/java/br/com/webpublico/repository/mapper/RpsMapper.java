package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoRpsNfseDTO;
import com.beust.jcommander.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RpsMapper implements RowMapper<RpsNfseDTO> {

    @Override
    public RpsNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        RpsNfseDTO dto = new RpsNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setNumero(resultSet.getString("NUMERO"));
        dto.setSerie(resultSet.getString("SERIE"));
        dto.setDataEmissao(resultSet.getDate("DATAEMISSAO"));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("PRESTADOR_ID"));
        dto.setNaturezaOperacao(ExigibilidadeNfseDTO.valueOf(resultSet.getString("NATUREZAOPERACAO")));
        dto.setOptanteSimplesNacional(resultSet.getBoolean("OPTANTESIMPLESNACIONAL"));
        dto.setCompetencia(resultSet.getDate("COMPETENCIA"));
        dto.setDadosPessoaisPrestador(new DadosPessoaisNfseDTO());
        dto.getDadosPessoaisPrestador().setId(resultSet.getLong("DADOSPESSOAISPRESTADOR_ID"));
        if (resultSet.getLong("DADOSPESSOAISTOMADOR_ID") != 0) {
            dto.setDadosPessoaisTomador(new DadosPessoaisNfseDTO());
            dto.getDadosPessoaisTomador().setId(resultSet.getLong("DADOSPESSOAISTOMADOR_ID"));
        }
        dto.setTributosFederais(new TributosFederaisNfseDTO());
        dto.getTributosFederais().setId(resultSet.getLong("TRIBUTOSFEDERAIS_ID"));
        dto.setIssRetido(resultSet.getBoolean("ISSRETIDO"));
        dto.setTotalServicos(resultSet.getBigDecimal("TOTALSERVICOS"));
        dto.setDeducoesLegais(resultSet.getBigDecimal("DEDUCOESLEGAIS"));
        dto.setDescontosIncondicionais(resultSet.getBigDecimal("DESCONTOSINCONDICIONAIS"));
        dto.setDescontosCondicionais(resultSet.getBigDecimal("DESCONTOSCONDICIONAIS"));
        dto.setRetencoesFederais(resultSet.getBigDecimal("RETENCOESFEDERAIS"));
        dto.setBaseCalculo(resultSet.getBigDecimal("BASECALCULO"));
        dto.setIssCalculado(resultSet.getBigDecimal("ISSCALCULADO"));
        dto.setIssPagar(resultSet.getBigDecimal("ISSPAGAR"));
        dto.setValorLiquido(resultSet.getBigDecimal("VALORLIQUIDO"));
        dto.setIdLote(resultSet.getLong("LOTERPS_ID"));
        if (dto.getIdLote() != null) {
            dto.setNumeroLote(resultSet.getString("NUMERO_LOTE"));
        }
        dto.setDescriminacaoServico(resultSet.getString("DESCRIMINACAOSERVICO"));
        if (resultSet.getString("TIPORPS") != null && !resultSet.getString("TIPORPS").isEmpty())
            dto.setTipoRps(TipoRpsNfseDTO.valueOf(resultSet.getString("TIPORPS")));
        dto.setIncentivoFiscal(resultSet.getBoolean("INCENTIVOFISCAL"));
        if (resultSet.getLong("CONSTRUCAOCIVIL_ID") != 0) {
            dto.setConstrucaoCivil(new ConstrucaoCivilNfseDTO());
            dto.getConstrucaoCivil().setId(resultSet.getLong("CONSTRUCAOCIVIL_ID"));
        }
        dto.setNumeroNotaFiscal(resultSet.getString("NUMERO_NOTA_FISCAL"));
        if (!Strings.isStringEmpty(resultSet.getString("SITUACAO_NOTA_FISCAL"))) {
            dto.setSituacao(SituacaoDeclaracaoNfseDTO.valueOf(resultSet.getString("SITUACAO_NOTA_FISCAL")));
        }
        dto.setIdNotaFiscal(resultSet.getLong("ID_NOTA_FISCAL"));
        return dto;
    }
}
