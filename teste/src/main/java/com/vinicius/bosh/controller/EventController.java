package com.vinicius.bosh.controller;


import com.vinicius.bosh.model.Event;
import com.vinicius.bosh.model.dto.EventDto;
import com.vinicius.bosh.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    private EventService service;


    @GetMapping
    public Flux<Event> findEvents() {
        return service.findEvents();
    }

    @GetMapping("/{id}")
    public Mono<Event> findEvent(@PathVariable Long id) {
        return service.findEventById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> create(@RequestBody EventDto eventDto) {
        return service.register(eventDto)
                .map(event -> ResponseEntity.created(URI.create("/events/" + event.getId())).build());
    }

    @PutMapping("/{id}")
    public Mono<Event> updateEvent(@RequestBody EventDto eventDto, @PathVariable Long id) {
        return service.updateEvent(eventDto, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEvent(@PathVariable Long id) {
        return service.deleteEvent(id);
    }
}
