package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.ServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeferimentoNfseDTO;
import br.com.webpublico.repository.mapper.DeclaracaoPrestacaoServicoMapper;
import br.com.webpublico.repository.setter.DeclaracaoPrestacaoServicoDeclaradoSetter;
import br.com.webpublico.repository.setter.DeclaracaoPrestacaoServicoNfseSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DeclaracaoPrestacaoServicoJDBCRepository implements Serializable {

    private static final String SQL_FIELDS = " SELECT " +
            "       ID,   " +
            "       NATUREZAOPERACAO,   " +
            "       OPTANTESIMPLESNACIONAL,   " +
            "       COMPETENCIA,   " +
            "       INTERMEDIARIO_ID,   " +
            "       CONSTRUCAOCIVIL_ID,   " +
            "       CONDICAOPAGAMENTO_ID,   " +
            "       TRIBUTOSFEDERAIS_ID,   " +
            "       ISSRETIDO,   " +
            "       RESPONSAVELRETENCAO,   " +
            "       DADOSPESSOAISPRESTADOR_ID,   " +
            "       DADOSPESSOAISTOMADOR_ID,   " +
            "       TIPODOCUMENTO,   " +
            "       DEDUCOESLEGAIS,   " +
            "       DESCONTOSINCONDICIONAIS,   " +
            "       DESCONTOSCONDICIONAIS,   " +
            "       VALORLIQUIDO,   " +
            "       RETENCOESFEDERAIS,   " +
            "       TOTALSERVICOS,   " +
            "       BASECALCULO,   " +
            "       MODALIDADE,   " +
            "       SITUACAO,   " +
            "       ISSCALCULADO,   " +
            "       ISSPAGAR," +
            "       DADOSPESSOAISPRESTADOR_ID," +
            "       DADOSPESSOAISTOMADOR_ID," +
            "       ORIGEMEMISSAO ";
    private static final String SQL_FROM = " FROM DECLARACAOPRESTACAOSERVICO DEC ";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public DeclaracaoPrestacaoServicoNfseDTO findById(Long id) {
        List<DeclaracaoPrestacaoServicoNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                        " WHERE DEC.ID = ? ",
                new Object[]{id},
                new DeclaracaoPrestacaoServicoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public void inserirDeclaracaoPrestacaoServico(NotaFiscalNfseDTO dto) {
        dto.getDeclaracaoPrestacaoServico().setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO DECLARACAOPRESTACAOSERVICO " +
                        "(ID, NATUREZAOPERACAO, OPTANTESIMPLESNACIONAL, COMPETENCIA, INTERMEDIARIO_ID,  " +
                        "CONSTRUCAOCIVIL_ID, CONDICAOPAGAMENTO_ID, TRIBUTOSFEDERAIS_ID, ISSRETIDO,  " +
                        "RESPONSAVELRETENCAO, DADOSPESSOAISPRESTADOR_ID, DADOSPESSOAISTOMADOR_ID,  " +
                        "TIPODOCUMENTO, DEDUCOESLEGAIS, DESCONTOSINCONDICIONAIS, DESCONTOSCONDICIONAIS, " +
                        "VALORLIQUIDO, RETENCOESFEDERAIS, TOTALSERVICOS, BASECALCULO, MODALIDADE, SITUACAO,  " +
                        "ISSCALCULADO, ISSPAGAR, TOTALNOTA, ORIGEMEMISSAO) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new DeclaracaoPrestacaoServicoNfseSetter(dto));
    }

    public void inserirDeclaracaoPrestacaoServico(DeclaracaoPrestacaoServicoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO DECLARACAOPRESTACAOSERVICO " +
                        "(ID, NATUREZAOPERACAO, OPTANTESIMPLESNACIONAL, COMPETENCIA, INTERMEDIARIO_ID,  " +
                        "CONSTRUCAOCIVIL_ID, CONDICAOPAGAMENTO_ID, TRIBUTOSFEDERAIS_ID, ISSRETIDO,  " +
                        "RESPONSAVELRETENCAO, DADOSPESSOAISPRESTADOR_ID, DADOSPESSOAISTOMADOR_ID,  " +
                        "TIPODOCUMENTO, DEDUCOESLEGAIS, DESCONTOSINCONDICIONAIS, DESCONTOSCONDICIONAIS, " +
                        "VALORLIQUIDO, RETENCOESFEDERAIS, TOTALSERVICOS, BASECALCULO, MODALIDADE, SITUACAO,  " +
                        "CANCELAMENTO_ID, CNAE_ID, ISSCALCULADO, ISSPAGAR) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                dto);
    }

    public void inserirDeclaracaoPrestacaoServico(ServicoDeclaradoNfseDTO dto) {
        dto.getDeclaracaoPrestacaoServico().setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO DECLARACAOPRESTACAOSERVICO " +
                        "(ID, NATUREZAOPERACAO, OPTANTESIMPLESNACIONAL, COMPETENCIA, INTERMEDIARIO_ID,  " +
                        "CONSTRUCAOCIVIL_ID, CONDICAOPAGAMENTO_ID, TRIBUTOSFEDERAIS_ID, ISSRETIDO,  " +
                        "RESPONSAVELRETENCAO, DADOSPESSOAISPRESTADOR_ID, DADOSPESSOAISTOMADOR_ID,  " +
                        "TIPODOCUMENTO, DEDUCOESLEGAIS, DESCONTOSINCONDICIONAIS, DESCONTOSCONDICIONAIS, " +
                        "VALORLIQUIDO, RETENCOESFEDERAIS, TOTALSERVICOS, BASECALCULO, MODALIDADE, SITUACAO,  " +
                        "CNAE_ID, MIGRADO, ISSCALCULADO, ISSPAGAR, TOTALNOTA, ORIGEMEMISSAO) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new DeclaracaoPrestacaoServicoDeclaradoSetter(dto));
    }


    public void cancelar(DeclaracaoPrestacaoServicoNfseDTO declaracao) {
        jdbcTemplate.batchUpdate(" UPDATE DECLARACAOPRESTACAOSERVICO SET SITUACAO = ? " +
                        " WHERE ID = ?  ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        if (SituacaoDeferimentoNfseDTO.DEFERIDO.equals(declaracao.getCancelamento().getSituacaoFiscal())) {
                            preparedStatement.setString(1, SituacaoDeclaracaoNfseDTO.CANCELADA.name());
                        } else {
                            preparedStatement.setString(1, declaracao.getSituacao().name());
                        }
                        preparedStatement.setLong(2, declaracao.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    public void delete(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        jdbcTemplate.update(" delete from cancdeclaprestacaoservico where declaracao_id = ? ", declaracaoPrestacaoServico.getId());
        jdbcTemplate.update(" update declaracaoprestacaoservico set dadospessoaistomador_id = null, " +
                " dadospessoaisprestador_id = null where id = ? ", declaracaoPrestacaoServico.getId());
        jdbcTemplate.update(" delete from declaracaoprestacaoservico where id = ? ", declaracaoPrestacaoServico.getId());
        if (declaracaoPrestacaoServico.getDadosPessoaisPrestador() != null) {
            jdbcTemplate.update(" delete from dadospessoaisnfse where id = ? ", declaracaoPrestacaoServico.getDadosPessoaisPrestador().getId());
        }
        if (declaracaoPrestacaoServico.getDadosPessoaisTomador() != null) {
            jdbcTemplate.update(" delete from dadospessoaisnfse where id = ? ", declaracaoPrestacaoServico.getDadosPessoaisTomador().getId());
        }
    }
}
