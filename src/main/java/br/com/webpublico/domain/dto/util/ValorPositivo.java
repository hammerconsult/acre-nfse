package br.com.webpublico.domain.dto.util;


import br.com.webpublico.domain.dto.exception.TipoValidacao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ValorPositivo {

    TipoValidacao tipoValidacao();

    boolean permiteZero() default true;

}
