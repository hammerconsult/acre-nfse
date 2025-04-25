package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.XmlWsNfseRecebido;
import br.com.webpublico.util.Util;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XmlWsNfseSetter implements BatchPreparedStatementSetter {

    private XmlWsNfseRecebido xmlWsNfseRecebido;

    public XmlWsNfseSetter(XmlWsNfseRecebido xmlWsNfseRecebido) {
        this.xmlWsNfseRecebido = xmlWsNfseRecebido;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, xmlWsNfseRecebido.getId());
        ps.setDate(2, DateUtils.toSQLDate(xmlWsNfseRecebido.getDataRegistro()));
        ps.setString(3, xmlWsNfseRecebido.getInscricaoMunicipal());
        ps.setString(4, xmlWsNfseRecebido.getXml());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
