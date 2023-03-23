package com.vinicius.bosh.controller;

import com.vinicius.bosh.model.Event;
import com.vinicius.bosh.service.EventService;
import com.vinicius.bosh.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @InjectMocks
    private EventController eventController;
    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testFindEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setEventType("Event 1");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setEventType("Event 2");

        List<Event> eventList = Arrays.asList(event1, event2);

        when(eventService.findEvents()).thenReturn(Flux.just(event1, event2));

        Flux<Event> result = eventController.findEvents();

        StepVerifier.create(result)
                .expectNext(event1)
                .expectNext(event2)
                .verifyComplete();
    }
}