package br.com.webpublico.domain.dto.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rodolfo on 06/11/17.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Etiqueta {
    String value();
}
