package br.com.webpublico.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ApiKeyRequestInterceptor implements ClientHttpRequestInterceptor {

    private String apiKey;

    public ApiKeyRequestInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();
        headers.add("API-KEY", apiKey);
        return execution.execute(httpRequest, body);
    }
}
