package com.vinicius.bosh.controller.handler;

import com.vinicius.bosh.model.Appliance;
import com.vinicius.bosh.service.ApplianceProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RequestHandler {

    @Autowired
    private ApplianceProducerService service;

    public Mono<ServerResponse> streamApplianceStatus(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(service.streamWeather(), Appliance.class);
    }
}
