package com.vinicius.bosh.service;


import com.vinicius.bosh.model.Event;
import com.vinicius.bosh.model.dto.EventDto;
import com.vinicius.bosh.model.mapper.EventMapper;
import com.vinicius.bosh.repository.EventRepository;
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
public class EventService {


    @Autowired
    private EventRepository repository;

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    public Mono<Event> register(EventDto eventDto) {
        log.info(String.format("Event type requested to be created %s", eventDto.getEventType()));
        if (StringUtils.isBlank(eventDto.getEventType())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.EVENT_MUST_TO_BE_INFORMED.getMessage()));
        }
        Event event = eventMapper.dtoToEvent(eventDto);
        event.setStatus(Status.ENABLED.getEventStatus());

        return repository.findByEventType(event.getEventType())
                .doOnSuccess(existingEvent -> {
                    log.info("Event returned from repository: " + existingEvent);
                })
                .flatMap(existingEvent -> {
                    if (existingEvent != null) {
                        log.warn(String.format("This event already exists with eventId: %s", existingEvent.getId()));
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format(ResponseMessage.EVENT_ALREADY_EXIST.getMessage(), existingEvent.getId())));
                    }
                    return Mono.just(existingEvent);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("There are no events of this type...");
                    log.info(String.format("Saving new event type %s with status ENABLED...", event.getEventType()));
                    return repository.save(event);
                }));
    }


    public Flux<Event> findEvents() {
        return repository.findAll()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT, "No events found.")));
    }

    public Mono<Event> findEventById(Long id) {
        return repository.findById(id)
                .doOnSuccess(existingEvent -> {
                    log.info("Event returned from repository: " + existingEvent);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID " + id + " not found.")))
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

    public Mono<Event> updateEvent(EventDto eventDto, Long id) {
        return repository.findById(id)
                .doOnSuccess(existingEvent -> {
                    log.info("Event returned from repository: " + existingEvent);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.EVENT_NOT_FOUND.getMessage())))
                .flatMap(event1 -> {
                    event1.setEventType(eventDto.getEventType());
                    event1.setStatus(Status.ENABLED.getEventStatus());

                    return repository.findByEventType(eventDto.getEventType())
                            .hasElement()
                            .flatMap(hasElement -> {
                                if (hasElement) {
                                    log.warn(String.format("This event already exists with eventId: %s", event1.getId()));
                                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format(ResponseMessage.EVENT_ALREADY_EXIST.getMessage(), event1.getId())));
                                } else {
                                    return repository.save(event1);
                                }
                            });
                });
    }

    public Mono<Void> deleteEvent(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.EVENT_NOT_FOUND.getMessage())))
                .flatMap(repository::delete)
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        log.info("This event does not exist to be deleted...");
                        return Mono.error(error);
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR_SERVER.getMessage()));
                    }
                });
    }


    public Mono<String> findStatusById(Long eventId) {
        return repository.findEventStatusById(eventId).doOnSuccess(s -> log.info("The returned status was: " + s));
    }
}