package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoMunicipalNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.CodigoTributacaoMunicipalJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class CodigoTributacaoMunicipalService extends AbstractService<CodigoTributacaoMunicipalNfseDTO> {

    @Autowired
    CodigoTributacaoMunicipalJDBCRepository repository;
    @Autowired
    CodigoTributacaoService codigoTributacaoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(CodigoTributacaoMunicipalNfseDTO codigoTributacaoMunicipio) {
        if (codigoTributacaoMunicipio == null) {
            return;
        }
        if (codigoTributacaoMunicipio.getCodigoTributacao() != null) {
            codigoTributacaoMunicipio.setCodigoTributacao(codigoTributacaoService.findById(codigoTributacaoMunicipio.getCodigoTributacao().getId()));
        }
    }

    public CodigoTributacaoMunicipalNfseDTO findByIdCodigoTributacao(Long idCodigoTributacao) {
        CodigoTributacaoMunicipalNfseDTO codigoTributacaoMunicipio = repository.findByAtributo("obj.codigotributacao_id", idCodigoTributacao);
        preencher(codigoTributacaoMunicipio);
        return codigoTributacaoMunicipio;
    }

    public CodigoTributacaoMunicipalNfseDTO findByCodigoTributacaoAndVigencia(CodigoTributacaoNfseDTO codigoTributacao,
                                                                              Date inicioVigencia,
                                                                              Date fimVigencia) {
        CodigoTributacaoMunicipalNfseDTO codigoTributacaoMunicipio =
                repository.findByCodigoTributacaoAndVigencia(codigoTributacao, inicioVigencia, fimVigencia);
        preencher(codigoTributacaoMunicipio);
        return codigoTributacaoMunicipio;
    }

    public CodigoTributacaoMunicipalNfseDTO findByCodigoTributacaoAndAliquota(CodigoTributacaoNfseDTO codigoTributacao,
                                                                              BigDecimal aliquota) {
        CodigoTributacaoMunicipalNfseDTO codigoTributacaoMunicipio =
                repository.findByCodigoTributacaoAndAliquota(codigoTributacao, aliquota);
        preencher(codigoTributacaoMunicipio);
        return codigoTributacaoMunicipio;
    }
}
