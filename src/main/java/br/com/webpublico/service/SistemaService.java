package br.com.webpublico.service;

import br.com.webpublico.domain.dto.SistemaInfoNfseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SistemaService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(SistemaService.class);
    @Value("${app.version}")
    String appVersion;
    private SistemaInfoNfseDTO sistemaInfo;

    public SistemaInfoNfseDTO getSistemaInfo() {
        if (sistemaInfo == null)
            sistemaInfo = new SistemaInfoNfseDTO(appVersion);
        return sistemaInfo;
    }
}
