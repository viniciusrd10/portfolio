package com.vinicius.bosh.service;

import com.vinicius.bosh.model.Event;
import com.vinicius.bosh.model.dto.EventDto;
import com.vinicius.bosh.repository.EventRepository;
import com.vinicius.bosh.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterWithNewEvent() {
        EventDto eventDto = new EventDto();
        eventDto.setEventType("appliance.running");
        Event event = new Event();
        event.setEventType("test");
        event.setStatus(Status.ENABLED.getEventStatus());


        when(eventRepository.findByEventType(any(String.class))).thenReturn(Mono.empty());
        when(eventRepository.save(any(Event.class))).thenReturn(Mono.just(event));

        Mono<Event> result = eventService.register(eventDto);

        StepVerifier.create(result)
                .expectNextMatches(e -> e.getEventType().equals("test") && e.getStatus().equals(Status.ENABLED.getEventStatus()))
                .verifyComplete();

    }

    @Test
    void testRegisterWithExistingEvent() {
        EventDto eventDto = new EventDto();
        eventDto.setEventType("appliance.running");
        Event existingEvent = new Event();
        existingEvent.setId(1L);

        when(eventRepository.findByEventType(any(String.class))).thenReturn(Mono.just(existingEvent));

        Mono<Event> result = eventService.register(eventDto);

        StepVerifier.create(result)
                .expectErrorMatches(e ->
                        e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode().equals(HttpStatus.CONFLICT))
                .verify();
    }

    @Test
    void testRegisterWithInvalidEventType() {
        EventDto eventDto = new EventDto();
        eventDto.setEventType("");

        Mono<Event> result = eventService.register(eventDto);

        StepVerifier.create(result)
                .expectErrorMatches(e ->
                        e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode().equals(HttpStatus.BAD_REQUEST))
                .verify();
    }


    @Test
    void findEventsWithNoEvent() {

        when(eventRepository.findAll()).thenReturn(Flux.empty());

        Flux<Event> results = eventService.findEvents();

        StepVerifier.create(results)
                .expectErrorMatches(e ->
                        e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode().equals(HttpStatus.NO_CONTENT))
                .verify();

    }

    @Test
    void findEvents() {
        Event event = new Event();
        event.setId(new Random().nextLong());
        event.setEventType("test");
        event.setStatus(Status.ENABLED.getEventStatus());

        Flux<Event> fluxEvent = Flux.just(event, event, event, event);

        when(eventRepository.findAll()).thenReturn(fluxEvent);

        Flux<Event> results = eventService.findEvents();

        StepVerifier.create(results)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void findEventByIdWhenTheEventExist() {
        Event event = new Event();
        event.setId(1L);


        when(eventRepository.findById(any(Long.class))).thenReturn(Mono.just(event));

        Mono<Event> result = eventService.findEventById(1L);

        StepVerifier.create(result)
                .expectNextMatches(e -> e.getId().equals(1L))
                .verifyComplete();

    }

    @Test
    void findEventByIdWhenTheEventDoesNotExist() {

        when(eventRepository.findById(any(Long.class))).thenReturn(Mono.empty());

        Mono<Event> result = eventService.findEventById(2L);

        StepVerifier.create(result)
                .expectErrorMatches(e ->
                        e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode().equals(HttpStatus.NOT_FOUND))
                .verify();

    }

    @Test
    void updateEventWhenExistTheEventIdAndThereIsNoEventTypeAlreadyCreated() {

        EventDto eventDto = new EventDto();
        eventDto.setEventType("appliance.new");

        Event existingEvent = new Event();
        existingEvent.setEventType("appliance.old");
        existingEvent.setId(1l);

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setEventType("appliance.new");

        when(eventRepository.findByEventType(eventDto.getEventType())).thenReturn(Mono.empty());
        when(eventRepository.findById(1L)).thenReturn(Mono.just(existingEvent));
        when(eventRepository.save(any())).thenReturn(Mono.just(updatedEvent));

        Mono<Event> result = eventService.updateEvent(eventDto, 1L);

        StepVerifier.create(result)
                .expectNext(updatedEvent)
                .verifyComplete();
    }

    @Test
    void notUpdateEventWhenExistTheEventTypeAssociatedWithAnotherEventId() {

        EventDto eventDto = new EventDto();
        eventDto.setEventType("appliance.running");
        Event event = new Event();
        event.setEventType("appliance.running");
        event.setId(1l);
        event.setStatus(Status.ENABLED.getEventStatus());

        when(eventRepository.findByEventType(any(String.class))).thenReturn(Mono.just(event));
        when(eventRepository.findById(any(Long.class))).thenReturn(Mono.just(event));

        Mono<Event> result = eventService.updateEvent(eventDto, 1L);

        StepVerifier.create(result)
                .expectErrorMatches(e ->
                        e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode().equals(HttpStatus.CONFLICT))
                .verify();
    }

    @Test
    void notUpdateEventWhenThereIsNoEventId() {

        EventDto eventDto = new EventDto();
        eventDto.setEventType("appliance.running");


        when(eventRepository.findById(any(Long.class))).thenReturn(Mono.empty());

        Mono<Event> result = eventService.updateEvent(eventDto, 1L);

        StepVerifier.create(result)
                .expectErrorMatches(e ->
                        e instanceof ResponseStatusException && ((ResponseStatusException) e).getStatusCode().equals(HttpStatus.NOT_FOUND))
                .verify();
    }

    @Test
    void deleteEventWhenThereIsEventId() {
        Event event = new Event();
        event.setId(1l);

        when(eventRepository.findById(1l)).thenReturn(Mono.just(event));
        when(eventRepository.delete(any(Event.class))).thenReturn(Mono.empty());

        Mono<Void> result = eventService.deleteEvent(1l);

        StepVerifier.create(result)
                .verifyComplete();

        verify(eventRepository, times(1)).findById(1l);
        verify(eventRepository, times(1)).delete(eq(event));

    }

    @Test
    void findStatusById() {
        Long eventId = 1L;
        String expectedStatus = "ENABLED";
        when(eventRepository.findEventStatusById(eventId)).thenReturn(Mono.just(expectedStatus));

        Mono<String> result = eventService.findStatusById(eventId);

        StepVerifier.create(result)
                .expectNext(expectedStatus)
                .verifyComplete();

        verify(eventRepository, times(1)).findEventStatusById(eventId);
    }
}