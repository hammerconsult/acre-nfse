package br.com.webpublico.service;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.CosifNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.CosifJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CosifService extends AbstractService<CosifNfseDTO> {


    @Autowired
    CosifJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public CosifNfseDTO findByConta(String conta) {
        conta = StringUtils.getApenasNumeros(conta);
        return repository.findByAtributo(" regexp_replace(obj.conta, '\\D','') ", conta);
    }

    public List<CosifNfseDTO> findByGrupo(String grupo) {
        return repository.findByGrupo(grupo);
    }
}
