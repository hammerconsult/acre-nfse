package br.com.webpublico.service;

import br.com.webpublico.domain.dto.PaisNfseDTO;
import br.com.webpublico.repository.PaisJDBCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaisService extends AbstractWPService<PaisNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(MunicipioService.class);

    @Autowired
    private PaisJDBCRepository paisJDBCRepository;

    public PaisNfseDTO findByCodigo(String codigo) {
        return paisJDBCRepository.findByCodigo(codigo);
    }

    public PaisNfseDTO findByNome(String nome) {
        return findByAtribute("nome", nome);
    }

    @Override
    public String getTableName() {
        return "Pais";
    }

    @Override
    public String getDefaltSearchFields() {
        return "nome";
    }

    @Override
    public ParameterizedTypeReference<List<PaisNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<PaisNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<PaisNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<PaisNfseDTO>() {
        };
    }
}
