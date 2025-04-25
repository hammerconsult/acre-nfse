package br.com.webpublico.service;


import br.com.webpublico.config.UrlsProperties;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public abstract class AbstractWPService<T> {

    private final Logger log = LoggerFactory.getLogger(AbstractWPService.class);
    public static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    protected UrlsProperties urlsProperties;

    public ResponseEntity<List<T>> findAll(Pageable pageable) {
        return findByQuery("", pageable);
    }

    public ResponseEntity<List<T>> findByQuery(String query) {
        return findByQuery(query, null);
    }

    public ResponseEntity<List<T>> findByQuery(String query, Pageable pageable) {
        if (pageable == null) {
            pageable = PaginationUtil.generatePageRequest(0, 100, Sort.by("id"));
        }
        String url = PaginationUtil.addParamsToUrl(urlsProperties.getWebpublicoPathNfse() + "/pesquisa-generica",
                pageable, query, getTableName(), getDefaltSearchFields());
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    public ResponseEntity<T> findOne(Long id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/pesquisa-generica/registro";
        url = UriComponentsBuilder.fromUriString(url).queryParam("table", getTableName())
                .queryParam("id", id).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseType());
    }

    public T findByAtribute(String name, Object value) {
        try {
            Pageable pageable = PaginationUtil.generatePageRequest(0, 1);
            String url = PaginationUtil.addParamsToUrl(urlsProperties.getWebpublicoPathNfse() + "/pesquisa-generica/first",
                    pageable, value.toString(), getTableName(), name);
            return restTemplate.exchange(url, HttpMethod.GET, null, getResponseType()).getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public abstract String getTableName();

    public abstract String getDefaltSearchFields();


    public abstract ParameterizedTypeReference<List<T>> getResponseTypeList();

    public abstract ParameterizedTypeReference<T> getResponseType();


}
