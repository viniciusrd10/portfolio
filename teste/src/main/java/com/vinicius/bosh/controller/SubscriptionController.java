package com.vinicius.bosh.controller;


import com.vinicius.bosh.model.Subscription;
import com.vinicius.bosh.model.dto.SubscriptionDto;
import com.vinicius.bosh.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;


    @GetMapping
    public Flux<Subscription> findSubscriptions(){
        return service.findAllSubscriptions();
    }

    @GetMapping("/{id}")
    public Mono<Subscription> findSubscription(@PathVariable Long id){
        return service.findSubscriptionById(id);
    }

    @PostMapping
    public Mono<Subscription> register(@RequestBody SubscriptionDto subscriptionDto) {
        return service.saveIfEventEnabled(subscriptionDto);
    }

    @PutMapping("/{id}")
    public Mono<Subscription> updateSubscription(@RequestBody SubscriptionDto subscriptionDto, @PathVariable Long id) {
        return service.updateSubscription(subscriptionDto, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSubscription(@PathVariable Long id) {
        return service.deleteSubscription(id);
    }
}
