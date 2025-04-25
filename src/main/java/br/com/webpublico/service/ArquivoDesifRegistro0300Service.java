package br.com.webpublico.service;

import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0300NfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifRegistro0300JDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArquivoDesifRegistro0300Service extends AbstractService<ArquivoDesifRegistro0300NfseDTO> {

    @Autowired
    ArquivoDesifRegistro0300JDBCRepository repository;
    @Autowired
    ProdutoServicoBancarioService produtoServicoBancarioService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoDesifRegistro0300NfseDTO registro0300) {
        if (registro0300 != null) {
            registro0300.setProdutoServicoBancario(produtoServicoBancarioService.findById(registro0300.getProdutoServicoBancario().getId()));
        }
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        repository.removerTodosPorIdArquivo(idArquivo);
    }

    public List<ArquivoDesifRegistro0300NfseDTO> findByIdArquivo(Long idArquivoDesif) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery(
                Lists.newArrayList(new ParametroQueryCampo("obj.arquivodesif_id", Operador.IGUAL, idArquivoDesif))
        );
        return repository.consultar(null, Lists.newArrayList(parametroQuery), null);
    }
}
