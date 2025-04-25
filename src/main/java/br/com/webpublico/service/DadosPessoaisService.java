package br.com.webpublico.service;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.repository.DadosPessoaisJDBCRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class DadosPessoaisService implements Serializable {

    @Autowired
    private DadosPessoaisJDBCRepository dadosPessoaisJDBCRepository;
    @Autowired
    private CidadeService cidadeService;

    public DadosPessoaisNfseDTO findById(Long id) {
        DadosPessoaisNfseDTO dto = dadosPessoaisJDBCRepository.findById(id);
        if (dto != null) {
            if (dto.getMunicipio() != null && !Strings.isNullOrEmpty(dto.getMunicipio().getCodigoIbge())) {
                dto.setMunicipio(cidadeService.findByCodigoIBGE(dto.getMunicipio().getCodigoIbge()));
            }
        }
        return dto;
    }

    public DadosPessoaisNfseDTO findByCpfCnpj(String cpfCnpj) {
        DadosPessoaisNfseDTO dto = dadosPessoaisJDBCRepository.findByCpfCnpj(cpfCnpj);
        if (dto != null) {
            if (dto.getMunicipio() != null && !Strings.isNullOrEmpty(dto.getMunicipio().getCodigoIbge())) {
                dto.setMunicipio(cidadeService.findByCodigoIBGE(dto.getMunicipio().getCodigoIbge()));
            }
        }
        return dto;
    }

    public void inserir(DadosPessoaisNfseDTO dto) {
        dadosPessoaisJDBCRepository.inserirDadosPessoais(dto);
    }

    public void salvar(DadosPessoaisNfseDTO dto) {

        if (dto.getId() == null) {
            dadosPessoaisJDBCRepository.inserirDadosPessoais(dto);
        } else {
            dadosPessoaisJDBCRepository.atualizarDadosPessoais(dto);
        }
    }
}
