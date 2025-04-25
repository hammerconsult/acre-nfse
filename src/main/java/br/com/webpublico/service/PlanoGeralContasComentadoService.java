package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoProdutoServicoNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoTarifaBancariaNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.PlanoGeralContasComentadoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class PlanoGeralContasComentadoService extends AbstractService<PlanoGeralContasComentadoNfseDTO> {

    @Autowired
    private PlanoGeralContasComentadoJDBCRepository repository;
    @Autowired
    private CosifService cosifService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private CodigoTributacaoService codigoTributacaoService;
    @Autowired
    private TarifaBancariaService tarifaBancariaService;
    @Autowired
    private ProdutoServicoBancarioService produtoServicoBancarioService;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(PlanoGeralContasComentadoNfseDTO registro) {
        if (registro == null) {
            return;
        }
        if (registro.getPrestador() != null) {
            registro.setPrestador(cadastroEconomicoService.findById(registro.getPrestador().getId()));
        }
        if (registro.getCosif() != null) {
            registro.setCosif(cosifService.findById(registro.getCosif().getId()));
        }
        if (registro.getServico() != null) {
            registro.setServico(servicoService.findById(registro.getServico().getId()));
        }
        if (registro.getCodigoTributacao() != null) {
            registro.setCodigoTributacao(codigoTributacaoService.findById(registro.getCodigoTributacao().getId()));
        }
        if (registro.getTarifaBancaria() != null) {
            registro.getTarifaBancaria().setTarifaBancaria(tarifaBancariaService.findById(registro.getTarifaBancaria().getTarifaBancaria().getId()));
        }
        if (registro.getProdutoServico() != null) {
            registro.getProdutoServico().setProdutoServico(produtoServicoBancarioService.findById(registro.getProdutoServico().getProdutoServico().getId()));
        }
    }

    public PlanoGeralContasComentadoNfseDTO findByContaAndDesdobramentoAndReferencia(Long prestador,
                                                                                     String conta,
                                                                                     String desdobramento,
                                                                                     Date referencia) {
        PlanoGeralContasComentadoNfseDTO dto = repository.findByContaAndDesdobramentoAndReferencia(prestador, conta, desdobramento, referencia);
        preencher(dto);
        return dto;
    }

    public List<PlanoGeralContasComentadoNfseDTO> buscarContasTributaveisNaoDeclaradas(Long prestador,
                                                                                       Long arquivo) {
        return repository.buscarContasTributaveisNaoDeclaradas(prestador, arquivo);
    }

    public void savePgccTarifaBancaria(PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        repository.savePgccTarifaBancaria(pgccTarifaBancaria);
    }

    public void changePgccTarifaBancaria(PlanoGeralContasComentadoNfseDTO pgcc,
                                         PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria) {
        repository.changePgccTarifaBancaria(pgcc, pgccTarifaBancaria);
    }

    public void savePgccProdutoServico(PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        repository.savePgccProdutoServico(pgccProdutoServico);
    }

    public void changePgccProdutoServico(PlanoGeralContasComentadoNfseDTO pgcc,
                                         PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico) {
        repository.changePgccProdutoServico(pgcc, pgccProdutoServico);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        PlanoGeralContasComentadoNfseDTO dto = findById(id);
        if (dto.getProdutoServico() != null) {
            changePgccProdutoServico(dto, null);
            repository.removePgccProdutoServico(dto.getProdutoServico());
        }
        if (dto.getTarifaBancaria() != null) {
            changePgccTarifaBancaria(dto, null);
            repository.removePgccTarifaBancaria(dto.getTarifaBancaria());
        }
        repository.remove(dto);
    }
}
