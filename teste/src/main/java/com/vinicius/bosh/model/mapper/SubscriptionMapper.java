package com.vinicius.bosh.model.mapper;

import com.vinicius.bosh.model.Subscription;
import com.vinicius.bosh.model.dto.SubscriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SubscriptionMapper {


    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "callbackURL", source = "callbackURL")
    Subscription dtoToSubscription(SubscriptionDto subscriptionDto);
    SubscriptionDto subscriptionToDto(Subscription subscription);
}
