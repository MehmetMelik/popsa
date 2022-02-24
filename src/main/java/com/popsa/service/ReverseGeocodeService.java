package com.popsa.service;

import com.popsa.config.HereApiConfig;
import com.popsa.data.Here;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ReverseGeocodeService {

    @Autowired
    private HereApiConfig hereApiConfig;

    @Autowired
    private final WebClient webClient;

    public ReverseGeocodeService(WebClient webClient, HereApiConfig hereApiConfig) {
        this.webClient = webClient;
        this.hereApiConfig = hereApiConfig;
    }

    public String[] getCity(double latitude, double longitude) {
        final WebClient.ResponseSpec response = webClient.get().uri(uriBuilder ->
                uriBuilder.path("/revgeocode")
                        .queryParam("apiKey", hereApiConfig.getHereApiKey())
                        .queryParam("at", latitude + "," + longitude)
                        .queryParam("lang", "en-US")
                        .build()).retrieve();
        final Mono<Here> stringMono = response.bodyToMono(Here.class);
        Here here = stringMono.block ();
        if (here != null && here.items().size() > 0) {
            final String city = here.items().get(0).address().city();
            final String countryName = here.items().get(0).address().countryName();
            return new String[]{city, countryName};
        } else
            return null;
    }
}
