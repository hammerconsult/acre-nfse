package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.OrigemEmissaoNfseDTO;
import br.com.webpublico.domain.dto.enums.ResponsavelRetencaoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeclaracaoPrestacaoServicoMapper implements RowMapper<DeclaracaoPrestacaoServicoNfseDTO> {


    @Override
    public DeclaracaoPrestacaoServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        DeclaracaoPrestacaoServicoNfseDTO dto = new DeclaracaoPrestacaoServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setNaturezaOperacao(ExigibilidadeNfseDTO.valueOf(resultSet.getString("NATUREZAOPERACAO")));
        dto.setOptanteSimplesNacional(resultSet.getBoolean("OPTANTESIMPLESNACIONAL"));
        dto.setCompetencia(resultSet.getDate("COMPETENCIA"));
        if (resultSet.getLong("INTERMEDIARIO_ID") != 0) {
            dto.setIntermediario(new IntermediarioServicoNfseDTO());
            dto.getIntermediario().setId(resultSet.getLong("INTERMEDIARIO_ID"));
        }
        if (resultSet.getLong("CONSTRUCAOCIVIL_ID") != 0) {
            dto.setConstrucaoCivil(new ConstrucaoCivilNfseDTO());
            dto.getConstrucaoCivil().setId(resultSet.getLong("CONSTRUCAOCIVIL_ID"));
        }
        if (resultSet.getLong("TRIBUTOSFEDERAIS_ID") != 0) {
            dto.setTributosFederais(new TributosFederaisNfseDTO());
            dto.getTributosFederais().setId(resultSet.getLong("TRIBUTOSFEDERAIS_ID"));
        }
        dto.setIssRetido(resultSet.getBoolean("ISSRETIDO"));
        if (resultSet.getString("RESPONSAVELRETENCAO") != null &&
                !resultSet.getString("RESPONSAVELRETENCAO").isEmpty()) {
            dto.setResponsavelRetencao(ResponsavelRetencaoNfseDTO.valueOf(resultSet.getString("RESPONSAVELRETENCAO")));
        }
        dto.setSituacao(SituacaoDeclaracaoNfseDTO.valueOf(resultSet.getString("SITUACAO")));
        if (resultSet.getLong("DADOSPESSOAISPRESTADOR_ID") != 0) {
            dto.setDadosPessoaisPrestador(new DadosPessoaisNfseDTO());
            dto.getDadosPessoaisPrestador().setId(resultSet.getLong("DADOSPESSOAISPRESTADOR_ID"));
        }
        if (resultSet.getLong("DADOSPESSOAISTOMADOR_ID") != 0) {
            dto.setDadosPessoaisTomador(new DadosPessoaisNfseDTO());
            dto.getDadosPessoaisTomador().setId(resultSet.getLong("DADOSPESSOAISTOMADOR_ID"));
        }
        if (!Strings.isNullOrEmpty(resultSet.getString("ORIGEMEMISSAO"))) {
            dto.setOrigemEmissao(OrigemEmissaoNfseDTO.valueOf(resultSet.getString("ORIGEMEMISSAO")));
        }
        dto.setTotalServicos(resultSet.getBigDecimal("TOTALSERVICOS"));
        dto.setBaseCalculo(resultSet.getBigDecimal("BASECALCULO"));
        dto.setDeducoesLegais(resultSet.getBigDecimal("DEDUCOESLEGAIS"));
        dto.setIssCalculado(resultSet.getBigDecimal("ISSCALCULADO"));
        dto.setIssPagar(resultSet.getBigDecimal("ISSPAGAR"));
        if (!Strings.isNullOrEmpty(resultSet.getString("MODALIDADE")))
            dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.valueOf(resultSet.getString("MODALIDADE")));
        if (!Strings.isNullOrEmpty(resultSet.getString("ORIGEMEMISSAO")))
            dto.setOrigemEmissao(OrigemEmissaoNfseDTO.valueOf(resultSet.getString("ORIGEMEMISSAO")));
        return dto;
    }
}
