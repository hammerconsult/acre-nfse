package br.com.webpublico.service;

import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.repository.DetalheItemDeclaracaoServicoJDBCRepository;
import br.com.webpublico.repository.ItemDeclaracaoServicoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ItemDeclaracaoPrestacaoServicoService implements Serializable {

    @Autowired
    private ItemDeclaracaoServicoJDBCRepository itemDeclaracaoServicoJDBCRepository;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private CidadeService cidadeService;
    @Autowired
    private PaisService paisService;
    @Autowired
    private DetalheItemDeclaracaoServicoJDBCRepository detalheItemDeclaracaoServicoJDBCRepository;
    @Autowired
    private PlanoGeralContasComentadoService planoGeralContasComentadoService;

    public List<ItemDeclaracaoServicoNfseDTO> findByDeclaracao(Long idDeclaracao) {
        List<ItemDeclaracaoServicoNfseDTO> itens = itemDeclaracaoServicoJDBCRepository.findByDeclaracao(idDeclaracao);
        if (itens != null) {
            for (ItemDeclaracaoServicoNfseDTO item : itens) {
                preencherItemDeclaracaoServico(item);
            }
        }
        return itens;
    }

    private void preencherItemDeclaracaoServico(ItemDeclaracaoServicoNfseDTO item) {
        if (item != null) {
            if (item.getServico() != null) {
                item.setServico(servicoService.findById(item.getServico().getId()));
            }
            if (item.getMunicipio() != null) {
                item.setMunicipio(cidadeService.findById(item.getMunicipio().getId()));
            }
            if (item.getPais() != null) {
                item.setPais(paisService.findOne(item.getPais().getId()).getBody());
            }
            if (item.getConta() != null) {
                item.setConta(planoGeralContasComentadoService.findById(item.getConta().getId()));
            }
            item.setDetalhes(detalheItemDeclaracaoServicoJDBCRepository.findByItem(item.getId()));
        }
    }

    public void inserir(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico, ItemDeclaracaoServicoNfseDTO item) {
        itemDeclaracaoServicoJDBCRepository.inserirItemDeclaracaoServico(declaracaoPrestacaoServico, item);
        detalheItemDeclaracaoServicoJDBCRepository.inserirDealhesDeclaracaoServico(item);
    }

    public void delete(ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico) {
        for (DetalheItemDeclaracaoServicoNfseDTO detalhe : itemDeclaracaoServico.getDetalhes()) {
            detalheItemDeclaracaoServicoJDBCRepository.delete(detalhe);
        }
        itemDeclaracaoServicoJDBCRepository.delete(itemDeclaracaoServico);
    }
}
