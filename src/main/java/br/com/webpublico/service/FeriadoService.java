package br.com.webpublico.service;

import br.com.webpublico.DateUtils;
import br.com.webpublico.repository.FeriadoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class FeriadoService implements Serializable {

    @Autowired
    private FeriadoJDBCRepository feriadoJDBCRepository;

    public boolean isFeriado(Date data) {
        return feriadoJDBCRepository.isFeriado(data);
    }

    public Date proximoDiaUtil(Date data) {
        if (DateUtils.isSabadoOrDomingo(data) || isFeriado(data)) {
            return proximoDiaUtil(DateUtils.adicionarDias(data, 1));
        }
        return data;
    }


}
