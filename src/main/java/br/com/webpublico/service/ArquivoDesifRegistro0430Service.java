package br.com.webpublico.service;

import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0430NfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifRegistro0430JDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArquivoDesifRegistro0430Service extends AbstractService<ArquivoDesifRegistro0430NfseDTO> {

    @Autowired
    ArquivoDesifRegistro0430JDBCRepository repository;
    @Autowired
    PlanoGeralContasComentadoService planoGeralContasComentadoService;
    @Autowired
    CodigoTributacaoService codigoTributacaoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoDesifRegistro0430NfseDTO registro) {
        if (registro.getId() != null) {
            if (registro.getConta() != null) {
                registro.setConta(planoGeralContasComentadoService.findById(registro.getConta().getId()));
            }
            if (registro.getCodigoTributacao() != null) {
                registro.setCodigoTributacao(codigoTributacaoService.findById(registro.getCodigoTributacao().getId()));
            }
        }
    }

    public void removerTodosPorIdArquivo(Long id) {
        repository.removerTodosPorIdArquivo(id);
    }

    public List<ArquivoDesifRegistro0430NfseDTO> findByIdArquivo(Long idArquivoDesif) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery(
                Lists.newArrayList(new ParametroQueryCampo("obj.arquivodesif_id", Operador.IGUAL, idArquivoDesif))
        );
        List<ArquivoDesifRegistro0430NfseDTO> registros = repository.consultar(null, Lists.newArrayList(parametroQuery), null);
        if (registros != null) {
            for (ArquivoDesifRegistro0430NfseDTO registro : registros) {
                preencher(registro);
            }
        }
        return registros;
    }
}
