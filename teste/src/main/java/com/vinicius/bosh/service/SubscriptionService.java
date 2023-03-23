package com.vinicius.bosh.service;

import com.vinicius.bosh.model.Subscription;
import com.vinicius.bosh.model.dto.SubscriptionDto;
import com.vinicius.bosh.model.mapper.SubscriptionMapper;
import com.vinicius.bosh.repository.SubscriptionRepository;
import com.vinicius.bosh.utils.ResponseMessage;
import com.vinicius.bosh.utils.Status;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository repository;

    @Autowired
    private EventService eventService;

    private SubscriptionMapper subscriptionMapper = Mappers.getMapper(SubscriptionMapper.class);

    public Mono<Subscription> saveIfEventEnabled(SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionMapper.dtoToSubscription(subscriptionDto);
        log.info("subscription to be saved: " + subscription);
        log.info("eventId to be associated with: " + subscription.getEventId());
        return eventService.findStatusById(subscriptionDto.getEventId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The event you are trying to subscribe does note exist.")))
                .flatMap(status -> {
                    log.info("Event status founded: " + status);
                    if (Status.ENABLED.getEventStatus().equals(status)) {
                        log.info("Creating subscription...");
                        return repository.save(subscription);
                    } else {
                        log.error("Event not enable to subscription.");
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ResponseMessage.EVENT_NOT_ENABLED.getMessage(), subscriptionDto.getEventId())));
                    }
                })
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    } else {
                        log.error("An unrecognized error occurred: " + error.getMessage());
                        log.error(error.getCause().toString());
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR_SERVER.getMessage()));
                    }
                })
                .doOnSuccess(s -> log.info("Subscription saved: {}", s.toString()));
    }

    public Flux<Subscription> findAllSubscriptions() {
        return repository.findAll()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT, "No subscriptions found.")));
    }

    public Mono<Subscription> findSubscriptionById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription with ID " + id + " not found.")))
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    } else {
                        log.error("An unrecognized error occurred: " + error.getMessage());
                        log.error(error.getCause().toString());
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR_SERVER.getMessage()));
                    }
                });
    }

    public Mono<Subscription> updateSubscription(SubscriptionDto subscriptionDto, Long id) {
        var mappedSubscription = subscriptionMapper.dtoToSubscription(subscriptionDto);
        log.info(String.format("subscriptionId informed: %s | eventId associated: %s", id, mappedSubscription.getEventId()));

        return eventService.findEventById(mappedSubscription.getEventId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.EVENT_NOT_FOUND.getMessage())))
                .flatMap(event -> {
                    log.info("Event status founded: " + event.getStatus());
                    if (!event.getStatus().equals(Status.ENABLED.getEventStatus())) {
                        log.error("database status: " + event.getStatus() + " enum status: " + Status.ENABLED.getEventStatus());
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ResponseMessage.EVENT_NOT_ENABLED.getMessage(), mappedSubscription.getEventId())));
                    }
                    log.info("Event enabled.");
                    log.info("Finding subscription by id");
                    return repository.findById(id)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.SUBSCRIPTION_NOT_FOUND.getMessage())))
                            .map(subscription -> {
                                log.info("Setting subscription eventId");
                                subscription.setEventId(mappedSubscription.getEventId());
                                log.info("Updating subscription");
                                return subscription;
                            })
                            .flatMap(repository::save);
                });
    }

    public Mono<Void> deleteSubscription(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.SUBSCRIPTION_NOT_FOUND.getMessage())))
                .flatMap(repository::delete)
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        log.info("This subscription does not exist to be deleted...");
                        return Mono.error(error);
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR_SERVER.getMessage()));
                    }
                });
    }

    //TODO retrieving data from ApplianceProducerService (like an Observer). Need adjustments to get only the status from producer.
    public Flux<String> findURLByEvenType(Flux<String> map) {
        return map.doOnEach(stringSignal -> log.info(stringSignal.toString()));
    }
}