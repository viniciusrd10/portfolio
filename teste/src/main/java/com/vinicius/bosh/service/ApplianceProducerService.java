package com.vinicius.bosh.service;

import com.vinicius.bosh.model.Appliance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Slf4j
public class ApplianceProducerService {

    private final Sinks.Many<Appliance> eventSink = Sinks.many().multicast().directBestEffort();

    public Flux<Appliance> streamWeather() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<Appliance> events = Flux
                .fromStream(Stream.generate(
                        () -> new Appliance(
                                getStatus(),
                                LocalDateTime
                                        .now())));
        return Flux.zip(events, interval, (key, value) -> {
            this.eventSink
                    .emitNext(key,((signalType, emitResult) -> {
                        log.warn("some events was lost");
                        return false;
                    }));
            log.info(key.toString());
            return key;
        });
    }

    private String getStatus() {
        String[] status = "running, waiting, fail, open, close, on, off".split(",");
        return status[new Random().nextInt(status.length)];
    }

}
