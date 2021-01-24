package br.com.v8technology.hdsc.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface IScheduleService {

    void checkingScheduleAvailability(LocalDateTime dateTime, String productType);

}
