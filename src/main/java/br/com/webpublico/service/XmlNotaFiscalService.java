package br.com.webpublico.service;

import br.com.webpublico.domain.dto.XmlNotaFiscalDTO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashSet;

@Service
public class XmlNotaFiscalService implements Serializable {

    private HashSet<XmlNotaFiscalDTO> gerandoXMl = new HashSet<>();

    public HashSet<XmlNotaFiscalDTO> getGerandoXMl() {
        return gerandoXMl;
    }

    public XmlNotaFiscalDTO get(String inscricaoCadastral) {
        return gerandoXMl
                .stream()
                .filter(xmlNotaFiscalDTO -> xmlNotaFiscalDTO.getInscricaoCadastral().equals(inscricaoCadastral))
                .findFirst()
                .get();
    }
}
