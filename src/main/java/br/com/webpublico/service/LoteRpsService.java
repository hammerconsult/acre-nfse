package br.com.webpublico.service;

import br.com.webpublico.domain.dto.LoteRpsNfseDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.repository.LoteRpsJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class LoteRpsService implements Serializable {

    @Autowired
    LoteRpsJDBCRepository loteRpsJDBCRepository;
    @Autowired
    CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    SistemaService sistemaService;

    public LoteRpsNfseDTO findById(Long id) {
        LoteRpsNfseDTO dto = loteRpsJDBCRepository.findById(id);
        preencher(dto);
        return dto;
    }

    public Page<LoteRpsNfseDTO> consultarLotesRps(Pageable pageable,
                                                  List<ParametroQuery> parametros) throws Exception {
        List<LoteRpsNfseDTO> dtos = loteRpsJDBCRepository.consultarLotesRps(pageable, parametros);
        for (LoteRpsNfseDTO dto : dtos) {
            preencher(dto);
        }

        Integer count = loteRpsJDBCRepository.contarLotesRps(parametros);

        return new PageImpl<>(dtos, pageable, count);
    }

    public void preencher(LoteRpsNfseDTO dto) {
        if (dto != null) {
            if (dto.getPrestador() != null) {
                dto.setPrestador(cadastroEconomicoService.findById(dto.getPrestador().getId()));
            }
        }
    }

    public String buscarXml(Long idLoteRps) {
        return loteRpsJDBCRepository.buscarXml(idLoteRps);
    }

    public String buscarLog(Long idLoteRps) {
        return loteRpsJDBCRepository.buscarLog(idLoteRps);
    }

}
