package br.com.webpublico.service;

import br.com.webpublico.domain.dto.DetentorArquivoComposicaoNfseDTO;
import br.com.webpublico.repository.DetentorArquivoComposicaoJDBCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class DetentorArquivoComposicaoService implements Serializable {
    private final Logger log = LoggerFactory.getLogger(DetentorArquivoComposicaoService.class);

    @Autowired
    private DetentorArquivoComposicaoJDBCRepository detentorArquivoComposicaoJDBCRepository;
    @Autowired
    private ArquivoComposicaoService arquivoComposicaoService;

    public DetentorArquivoComposicaoNfseDTO findById(Long id) {
        DetentorArquivoComposicaoNfseDTO dto = detentorArquivoComposicaoJDBCRepository.findById(id);
        if (dto.getArquivo() != null) {
            dto.setArquivo(arquivoComposicaoService.findById(dto.getArquivo().getId()));
        }
        dto.setArquivos(arquivoComposicaoService.findByDetentor(dto.getId()));
        return dto;
    }
}
