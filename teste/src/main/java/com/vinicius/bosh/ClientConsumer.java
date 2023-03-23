package com.vinicius.bosh;

import com.vinicius.bosh.model.Appliance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class ClientConsumer implements ApplicationRunner {

    //TODO running from another application (a external one) it get the events correctly.
    // Analyze the necessary adjustments to the the events form this app. It seems to be just a configuration on ApplicationRunner
    ApplicationRunner runner(WebClient webClient) {

        return args -> webClient.get()
                .uri("appliancestream")
                .retrieve()
                .bodyToFlux(Appliance.class)
                .subscribe(data -> log.info(data.toString()));
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("client")) {
           runner(WebClient.create("http://localhost:8080/"));
        }
    }
}
