package br.com.webpublico.web.rest.util;

import org.springframework.http.MediaType;

/**
 * Created by Clovis on 04/09/2015.
 */
public class MediaTypeWebPublico extends MediaType {

    public static final MediaType APPLICATION_PDF = valueOf("application/pdf");

    public MediaTypeWebPublico(String type) {
        super(type);
    }
}
