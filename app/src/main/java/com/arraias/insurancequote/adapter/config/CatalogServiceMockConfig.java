package com.arraias.insurancequote.adapter.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogServiceMockConfig {

    @Value("${wiremock.catalogservice.host}")
    private String host;

    @Value("${wiremock.catalogservice.port}")
    private int port;

    @PostConstruct
    public void configuration() {
        var catalogService = new WireMockServer(WireMockConfiguration.options()
                .port(port)
                .usingFilesUnderClasspath("wiremock"));
        WireMock.configureFor(host, port);
        catalogService.start();
    }

}
