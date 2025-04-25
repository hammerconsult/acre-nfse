package br.com.webpublico.service;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.ServicoNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ServicoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicoService extends AbstractService<ServicoNfseDTO> {

    @Autowired
    private ServicoJDBCRepository repository;
    @Autowired
    private AnexoLei1232006Service anexoLei1232006Service;

    public List<ServicoNfseDTO> findByIdCadastro(Long idCadastro) {
        List<ServicoNfseDTO> servicos = repository.findByIdCadastro(idCadastro);
        if (servicos != null) {
            for (ServicoNfseDTO servico : servicos) {
                preencher(servico);
            }
        }
        return servicos;
    }

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public ServicoNfseDTO findByCodigo(String codigo) {
        ServicoNfseDTO servico = repository.findByAtributo("obj.codigo", StringUtils.getApenasNumeros(codigo));
        preencher(servico);
        return servico;
    }

    @Override
    public void preencher(ServicoNfseDTO registro) {
        if (registro == null) return;
        if (registro.getAnexoLei1232006() != null) {
            registro.setAnexoLei1232006(anexoLei1232006Service.findById(registro.getAnexoLei1232006().getId()));
        }
    }
}
