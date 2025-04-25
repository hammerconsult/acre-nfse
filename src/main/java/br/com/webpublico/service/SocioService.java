package br.com.webpublico.service;

import br.com.webpublico.domain.dto.SocioNfseDTO;
import br.com.webpublico.repository.SocioJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class SocioService implements Serializable {

    @Autowired
    private SocioJDBCRepository socioJDBCRepository;
    @Autowired
    private PessoaService pessoaService;

    public List<SocioNfseDTO> findByIdCadastro(Long idCadastro) {
        List<SocioNfseDTO> socios = socioJDBCRepository.findByIdCadastro(idCadastro);
        if (socios != null && !socios.isEmpty()) {
            for (SocioNfseDTO socio : socios) {
                if (socio.getSocio() != null && socio.getSocio().getId() != null) {
                    socio.setSocio(pessoaService.findById(socio.getSocio().getId()));
                }
            }
        }
        return socios;
    }
}
