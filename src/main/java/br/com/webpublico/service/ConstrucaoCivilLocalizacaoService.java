package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConstrucaoCivilLocalizacaoNfseDTO;
import br.com.webpublico.repository.ConstrucaoCivilLocalizacaoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class ConstrucaoCivilLocalizacaoService implements Serializable {

    @Autowired
    private ConstrucaoCivilLocalizacaoJDBCRepository construcaoCivilLocalizacaoJDBCRepository;
    @Autowired
    private CidadeService cidadeService;

    public ConstrucaoCivilLocalizacaoNfseDTO findById(Long id) {
        ConstrucaoCivilLocalizacaoNfseDTO dto = construcaoCivilLocalizacaoJDBCRepository.findById(id);
        if (dto.getMunicipio() != null) {
            dto.setMunicipio(cidadeService.findById(dto.getMunicipio().getId()));
        }
        return dto;
    }
}
