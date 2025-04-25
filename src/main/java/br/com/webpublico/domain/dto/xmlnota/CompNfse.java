package br.com.webpublico.domain.dto.xmlnota;


import br.com.webpublico.domain.dto.nfse10.TcCancelamentoNfse;
import br.com.webpublico.domain.dto.nfse10.TcNfse;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "CompNfse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompNfse", propOrder = {
        "nfse",
        "nfseCancelamento"})
public class CompNfse {

    @XmlElement(name = "Nfse")
    private TcNfse nfse;
    @XmlElement(name = "NfseCancelamento")
    private TcCancelamentoNfse nfseCancelamento;

    public TcNfse getNfse() {
        return nfse;
    }

    public void setNfse(TcNfse nfse) {
        this.nfse = nfse;
    }

    public TcCancelamentoNfse getNfseCancelamento() {
        return nfseCancelamento;
    }

    public void setNfseCancelamento(TcCancelamentoNfse nfseCancelamento) {
        this.nfseCancelamento = nfseCancelamento;
    }
}
