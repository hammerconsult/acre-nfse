package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.NfseDTO;

import java.io.Serializable;

/**
 * Created by rodolfo on 09/10/17.
 */
public interface NfseEntity extends Serializable {

    NfseDTO toNfseDto();

}
