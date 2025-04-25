package br.com.webpublico.domain.dto.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.time.FastDateFormat;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rodolfo on 22/12/2016.
 */
public class Util {

    public static final Locale localeBrasil = new Locale("pt_BR_", "pt", "BR");
    public static final FastDateFormat formatterDiaMesAno = FastDateFormat.getInstance("dd/MM/yyyy", localeBrasil);


    public static String formataValor(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("###,##0.00");

        if (valor != null) {
            return "R$ " + df.format(valor);
        }
        return "";
    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String objectToXML(Object obj) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            //Print XML String to Console
            StringWriter sw = new StringWriter();
            //Write XML to StringWriter
            jaxbMarshaller.marshal(obj, sw);
            //Verify XML Content
            String xmlContent = sw.toString();

            return removerDadosNaoUtilizadosDoXMl(xmlContent);
        } catch (JAXBException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String removerDadosNaoUtilizadosDoXMl(String xml) {
        return xml
                .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")
                .replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "")
                .replace("xmlns=\"http://www.e-nfs.com.br\"", "")
                .replace("xmlns=\"https://nota.riobranco.ac.gov.br\"", "")
                .replace("xmlns=\"http://www.e-nf.com.br\"", "")
                .replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "")
                .replace("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "")
                .replace("xmlns=\"http://nfse.webpublico.com.br/iss/nfse_v1\"", "")
                .replace("xmlns=\"http://www.abrasf.org.br/nfse.xsd\"", "")
                .replace("xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"", "")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }

    public static Object xmlToObject(String xml, Class clazz) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Enum[] getValuesForEnum(Class clazz) {
        return (Enum[]) clazz.getEnumConstants();
    }

    public static boolean isFisica(String cpfCnpj) {
        return cpfCnpj.replaceAll("\\D+", "").length() == 11;
    }

    public static boolean isJuridica(String cpfCnpj) {
        return cpfCnpj.replaceAll("\\D+", "").length() == 14;
    }




    public static int getMes(Date dataAno) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataAno);
        return c.get(Calendar.MONTH) + 1;
    }

    public static Integer getAno(Date dataAno) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataAno);
        return c.get(Calendar.YEAR);
    }

    public static InputStream base64ToInputStream(String conteudo) {
        String data = "";
        try {
            data = conteudo.split("base64,")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            data = conteudo;
        }
        Base64 decoder = new Base64();
        byte[] imgBytes = decoder.decode(data);
        return new ByteArrayInputStream(imgBytes);
    }
}
