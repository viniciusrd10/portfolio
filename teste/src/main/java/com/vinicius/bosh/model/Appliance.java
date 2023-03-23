package com.vinicius.bosh.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("public.appliance")
@AllArgsConstructor
@Data
public class Appliance {

    private String status;
    private LocalDateTime eventTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
}
