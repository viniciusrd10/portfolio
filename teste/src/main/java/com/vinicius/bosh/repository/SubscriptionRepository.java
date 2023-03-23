package com.vinicius.bosh.repository;

import com.vinicius.bosh.model.Subscription;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends R2dbcRepository<Subscription, Long> {

}
