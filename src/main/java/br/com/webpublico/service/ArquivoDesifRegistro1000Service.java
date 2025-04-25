package br.com.webpublico.service;

import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro1000NfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifRegistro1000JDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArquivoDesifRegistro1000Service extends AbstractService<ArquivoDesifRegistro1000NfseDTO> {

    @Autowired
    ArquivoDesifRegistro1000JDBCRepository repository;
    @Autowired
    CidadeService cidadeService;
    @Autowired
    EventoContabilDesifService eventoContabilDesifService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoDesifRegistro1000NfseDTO registro1000) {
        if (registro1000 != null) {
            if (registro1000.getMunicipio() != null) {
                registro1000.setMunicipio(cidadeService.findById(registro1000.getMunicipio().getId()));
            }
            if (registro1000.getEventoContabil() != null) {
                registro1000.setEventoContabil(eventoContabilDesifService.findById(registro1000.getEventoContabil().getId()));
            }
            if (registro1000.getMunicipioVinculo() != null) {
                registro1000.setMunicipioVinculo(cidadeService.findById(registro1000.getMunicipioVinculo().getId()));
            }
        }
    }

    public void removerTodosPorIdArquivo(Long idArquivo) {
        repository.removerTodosPorIdArquivo(idArquivo);
    }

    public List<ArquivoDesifRegistro1000NfseDTO> findByIdArquivo(Long idArquivoDesif) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery(
                Lists.newArrayList(new ParametroQueryCampo("obj.arquivodesif_id", Operador.IGUAL, idArquivoDesif))
        );
        return repository.consultar(null, Lists.newArrayList(parametroQuery), null);
    }
}
