package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.util.Util;
import br.com.webpublico.web.rest.vm.EnumeradoVM;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * Created by romanini on 25/06/2018.
 */
@RestController
@RequestMapping("api/enumerado")
public class EnumeradoResource implements Serializable {
    private final Logger log = LoggerFactory.getLogger(EnumeradoResource.class);

    @PostMapping
    private ResponseEntity<List<EnumeradoVM>> buscarValoresEnum(@RequestBody String className) {
        List<EnumeradoVM> toReturn = Lists.newArrayList();
        try {
            Enum[] valuesForEnum;
            if (className.contains("br.com.webpublico")) {
                valuesForEnum = Util.getValuesForEnum(Class.forName(className));
            } else {
                valuesForEnum = Util.getValuesForEnum(Class.forName("br.com.webpublico.domain.dto.enums." + className));
            }
            if (valuesForEnum != null) {
                toReturn.add(new EnumeradoVM(null, ""));
                for (Enum anEnum : valuesForEnum) {
                    if (anEnum.name() != null && !anEnum.name().trim().isEmpty()) {
                        toReturn.add(new EnumeradoVM(anEnum.name(), anEnum.toString()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }
}
