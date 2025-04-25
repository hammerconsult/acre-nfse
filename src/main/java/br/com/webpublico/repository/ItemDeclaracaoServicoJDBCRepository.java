package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.repository.setter.ItemDeclaracaoServicoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ItemDeclaracaoServicoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public void inserirItemDeclaracaoServico(DeclaracaoPrestacaoServicoNfseDTO declaracaoDto,
                                             ItemDeclaracaoServicoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("INSERT INTO ITEMDECLARACAOSERVICO " +
                        "(ID, ISS, BASECALCULO, DEDUCOES, DESCONTOSINCONDICIONADOS, DESCONTOSCONDICIONADOS, " +
                        "QUANTIDADE, VALORSERVICO, DESCRICAO, NOMESERVICO, ALIQUOTASERVICO, PRESTADONOPAIS, " +
                        "OBSERVACOES, SERVICO_ID, MUNICIPIO_ID, DECLARACAOPRESTACAOSERVICO_ID, PAIS_ID, " +
                        "CNAE_ID, DEDUCAO, TIPODEDUCAO, TIPOOPERACAO, NUMERODOCUMENTOFISCAL, CPFCNPJDEDUCAO, " +
                        "VALORDOCUMENTOFISCAL, CONTA_ID, SALDOANTERIOR, CREDITO, DEBITO) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new ItemDeclaracaoServicoSetter(declaracaoDto, dto));
    }


    public List<ItemDeclaracaoServicoNfseDTO> findByDeclaracao(Long idDeclaracao) {
        return jdbcTemplate.query(" SELECT item.ID, item.ISS, item.BASECALCULO, item.DEDUCOES, item.DESCONTOSINCONDICIONADOS, item.DESCONTOSCONDICIONADOS," +
                        " item.QUANTIDADE, item.VALORSERVICO, item.DESCRICAO, item.NOMESERVICO, item.ALIQUOTASERVICO, item.PRESTADONOPAIS, item.OBSERVACOES," +
                        " item.SERVICO_ID, item.MUNICIPIO_ID, item.DECLARACAOPRESTACAOSERVICO_ID, item.PAIS_ID, item.DEDUCAO, item.TIPODEDUCAO, item.TIPOOPERACAO, item.NUMERODOCUMENTOFISCAL, item.CPFCNPJDEDUCAO, item.VALORDOCUMENTOFISCAL," +
                        " item.CONTA_ID, item.SALDOANTERIOR, item.CREDITO, item.DEBITO, servico.PERMITERECOLHIMENTOFORA " +
                        " FROM ITEMDECLARACAOSERVICO item " +
                        " left join servico on servico.id = item.servico_id " +
                        " WHERE item.DECLARACAOPRESTACAOSERVICO_ID = ? ",
                new Object[]{idDeclaracao},
                new ItemDeclaracaoServicoNfseDTO());
    }

    public List<ItemDeclaracaoServicoNfseDTO> findByRPS(Long idRps) {
        return jdbcTemplate.query(" SELECT item.ID, item.ISS, item.BASECALCULO, item.DEDUCOES, item.DESCONTOSINCONDICIONADOS, item.DESCONTOSCONDICIONADOS, " +
                        "       item.QUANTIDADE, item.VALORSERVICO, item.DESCRICAO, item.NOMESERVICO, item.ALIQUOTASERVICO, item.PRESTADONOPAIS, item.OBSERVACOES, " +
                        "       item.SERVICO_ID, item.MUNICIPIO_ID, null as DECLARACAOPRESTACAOSERVICO_ID, item.PAIS_ID, null as DEDUCAO, null as TIPODEDUCAO, null as TIPOOPERACAO, " +
                        "       null as NUMERODOCUMENTOFISCAL, null as CPFCNPJDEDUCAO, null as VALORDOCUMENTOFISCAL, " +
                        "       null as CONTA_ID, null as SALDOANTERIOR, null as CREDITO, null as DEBITO, servico.PERMITERECOLHIMENTOFORA " +
                        "    FROM ITEMRPS item " +
                        "   left join servico on servico.id = item.servico_id " +
                        " WHERE item.RPS_ID = ?",
                new Object[]{idRps},
                new ItemDeclaracaoServicoNfseDTO());
    }

    public void delete(ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico) {
        jdbcTemplate.update(" delete from itemdeclaracaoservico where id = ? ", itemDeclaracaoServico.getId());
    }
}
