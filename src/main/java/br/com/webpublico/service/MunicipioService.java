package br.com.webpublico.service;

import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.repository.CidadeJDBCRepository;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
public class MunicipioService extends AbstractWPService<MunicipioNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(MunicipioService.class);

    @Autowired
    CidadeJDBCRepository cidadeJDBCRepository;

    @Override
    public String getTableName() {
        return "Cidade";
    }

    @Override
    public String getDefaltSearchFields() {
        return "nome, to_char(codigoIBGE)";
    }

    @Override
    public ParameterizedTypeReference<List<MunicipioNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<MunicipioNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<MunicipioNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<MunicipioNfseDTO>() {
        };
    }

    @Override
    public ResponseEntity<List<MunicipioNfseDTO>> findAll(Pageable pageable) {
        UriComponentsBuilder uri = fromUriString(urlsProperties.getWebpublicoPathNfse() + "/cidades-by-filter");
        return restTemplate.exchange(uri.toUriString(), HttpMethod.GET, null, getResponseTypeList());
    }

    public Page<MunicipioNfseDTO> search(Pageable pageable, String filtro) throws Exception {
        if (filtro == null)
            filtro = "";
        List<ParametroQuery> parametros = Lists.newArrayList();
        parametros.add(new ParametroQuery(" and ", Lists.newArrayList(
                new ParametroQueryCampo("length(obj.codigoibge)", Operador.IGUAL, 7)
        )));
        parametros.add(new ParametroQuery(" or ", Lists.newArrayList(
                new ParametroQueryCampo("lower(to_char(obj.codigo))", Operador.LIKE, "%" + filtro.trim().toLowerCase() + "%"),
                new ParametroQueryCampo("lower(to_char(obj.nome))", Operador.LIKE, "%" + filtro.trim().toLowerCase() + "%")
        )));
        List<MunicipioNfseDTO> dtos = cidadeJDBCRepository.consultar(pageable, parametros, "");
        Integer count = cidadeJDBCRepository.contar(parametros);
        return new PageImpl<>(dtos, pageable, count);
    }
}
