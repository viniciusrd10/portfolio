package com.vinicius.bosh.repository;

import com.vinicius.bosh.model.Event;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface EventRepository extends R2dbcRepository<Event, Long> {

    @Query("SELECT * FROM event e WHERE event_type = $1")
    Mono<Event> findByEventType(String eventType);

    @Query("SELECT e.status FROM public.event e  WHERE e.event_id  = $1")
    Mono<String> findEventStatusById(Long eventId);
}
