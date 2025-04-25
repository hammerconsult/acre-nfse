package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.domain.dto.ArquivoParteNfseDTO;
import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoParteJDBCRepository;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArquivoParteService extends AbstractService<ArquivoParteNfseDTO> {

    @Autowired
    private ArquivoParteJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public List<ArquivoParteNfseDTO> findByArquivo(ArquivoNfseDTO arquivo) {
        ParametroQuery parametroQuery = new ParametroQuery(Lists.newArrayList(
                new ParametroQueryCampo("obj.arquivo_id", Operador.IGUAL, arquivo.getId())
        ));
        return repository.consultar(null, Lists.newArrayList(parametroQuery), null);
    }
}
