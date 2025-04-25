package br.com.webpublico.service;

import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0410NfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifRegistro0410JDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArquivoDesifRegistro0410Service extends AbstractService<ArquivoDesifRegistro0410NfseDTO> {

    @Autowired
    ArquivoDesifRegistro0410JDBCRepository repository;
    @Autowired
    PlanoGeralContasComentadoService planoGeralContasComentadoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoDesifRegistro0410NfseDTO registro) {
        if (registro != null) {
            if (registro.getConta() != null) {
                registro.setConta(planoGeralContasComentadoService.findById(registro.getConta().getId()));
            }
        }
    }

    public void removerTodosPorIdArquivo(Long id) {
        repository.removerTodosPorIdArquivo(id);
    }

    public List<ArquivoDesifRegistro0410NfseDTO> findByIdArquivo(Long idArquivoDesif) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery(
                Lists.newArrayList(new ParametroQueryCampo("obj.arquivodesif_id", Operador.IGUAL, idArquivoDesif))
        );
        return repository.consultar(null, Lists.newArrayList(parametroQuery), null);
    }
}
