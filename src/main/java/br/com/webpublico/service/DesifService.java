package br.com.webpublico.service;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.DesifNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.*;
import br.com.webpublico.repository.DesifJDBCRepository;
import br.com.webpublico.util.Util;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@Transactional
@Service
public class DesifService {

    @Autowired
    DesifJDBCRepository repository;
    @Autowired
    DeclaracaoPrestacaoServicoService declaracaoPrestacaoServicoService;
    @Autowired
    CadastroEconomicoService cadastroEconomicoService;

    public DesifNfseDTO inserir(DesifNfseDTO desif) {
        declaracaoPrestacaoServicoService.inserir(desif.getDeclaracaoPrestacaoServico());
        repository.insert(desif);
        return desif;
    }


}
