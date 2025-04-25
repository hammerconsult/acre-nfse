package br.com.webpublico.domain.dto.xmlnota;


import br.com.webpublico.domain.dto.xmlnota.CompNfse;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultarNfseLote", propOrder = {
        "listaNfse"
})
@XmlRootElement(name = "ConsultarNfseLote")
public class ConsultarNfseLote {

    @XmlElement(name = "ListaNfse")
    protected ListaNfse listaNfse;

    public ListaNfse getListaNfse() {
        return listaNfse;
    }

    public void setListaNfse(ListaNfse listaNfse) {
        this.listaNfse = listaNfse;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "compNfse"
    })
    public static class ListaNfse {

        @XmlElement(name = "CompNfse", required = true)
        protected List<CompNfse> compNfse;

        public List<CompNfse> getCompNfse() {
            return compNfse;
        }

        public void setCompNfse(List<CompNfse> compNfse) {
            this.compNfse = compNfse;
        }
    }
}
