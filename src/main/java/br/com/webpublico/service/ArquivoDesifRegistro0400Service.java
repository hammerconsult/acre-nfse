package br.com.webpublico.service;

import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0400NfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifRegistro0400JDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArquivoDesifRegistro0400Service extends AbstractService<ArquivoDesifRegistro0400NfseDTO> {

    @Autowired
    ArquivoDesifRegistro0400JDBCRepository repository;
    @Autowired
    TipoDependenciaDesifService tipoDependenciaDesifService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoDesifRegistro0400NfseDTO registro) {
        if (registro.getId() != null) {
            if (registro.getTipoDependencia() != null) {
                registro.setTipoDependencia(tipoDependenciaDesifService.findById(registro.getTipoDependencia().getId()));
            }
        }
    }

    public void removerTodosPorIdArquivo(Long id) {
        repository.removerTodosPorIdArquivo(id);
    }

    public List<ArquivoDesifRegistro0400NfseDTO> findByIdArquivo(Long idArquivoDesif) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery(
                Lists.newArrayList(new ParametroQueryCampo("obj.arquivodesif_id", Operador.IGUAL, idArquivoDesif))
        );
        return repository.consultar(null, Lists.newArrayList(parametroQuery), null);
    }
}
