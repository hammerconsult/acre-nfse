package br.com.webpublico.service;

import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0200NfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifRegistro0200JDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArquivoDesifRegistro0200Service extends AbstractService<ArquivoDesifRegistro0200NfseDTO> {

    @Autowired
    ArquivoDesifRegistro0200JDBCRepository repository;
    @Autowired
    TarifaBancariaService tarifaBancariaService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public void preencher(ArquivoDesifRegistro0200NfseDTO registro0200) {
        if (registro0200 != null) {
            registro0200.setTarifaBancaria(tarifaBancariaService.findById(registro0200.getTarifaBancaria().getId()));
        }
    }
    
    public void removerTodosPorIdArquivo(Long idArquivo) {
        repository.removerTodosPorIdArquivo(idArquivo);
    }

    public List<ArquivoDesifRegistro0200NfseDTO> findByIdArquivo(Long idArquivoDesif) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery(
                Lists.newArrayList(new ParametroQueryCampo("obj.arquivodesif_id", Operador.IGUAL, idArquivoDesif))
        );
        return repository.consultar(null, Lists.newArrayList(parametroQuery), null);
    }
}
