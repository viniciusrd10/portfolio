package com.vinicius.bosh.model.mapper;

import com.vinicius.bosh.model.Event;
import com.vinicius.bosh.model.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EventMapper {

    @Mapping(target = "eventType", source = "eventType")
    Event dtoToEvent(EventDto eventDto);
    EventDto eventToDto(Event event);
}
