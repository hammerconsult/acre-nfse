package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ArquivoComposicaoNfseDTO;
import br.com.webpublico.repository.ArquivoComposicaoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ArquivoComposicaoService implements Serializable {

    @Autowired
    private ArquivoComposicaoJDBCRepository arquivoComposicaoJDBCRepository;
    @Autowired
    private ArquivoService arquivoService;

    public ArquivoComposicaoNfseDTO findById(Long id) {
        ArquivoComposicaoNfseDTO dto = arquivoComposicaoJDBCRepository.findById(id);
        if (dto.getArquivoNfseDTO() != null) {
            dto.setArquivoNfseDTO(arquivoService.findById(dto.getArquivoNfseDTO().getId()));
        }
        return dto;
    }

    public List<ArquivoComposicaoNfseDTO> findByDetentor(Long idDetentor) {
        List<ArquivoComposicaoNfseDTO> dtos = arquivoComposicaoJDBCRepository.findByIdDetentor(idDetentor);
        if (dtos != null) {
            for (ArquivoComposicaoNfseDTO dto : dtos) {
                dto.setArquivoNfseDTO(arquivoService.findById(dto.getArquivoNfseDTO().getId()));
            }
        }
        return dtos;
    }

}
