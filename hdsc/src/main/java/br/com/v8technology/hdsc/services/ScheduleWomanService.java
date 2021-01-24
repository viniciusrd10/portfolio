package br.com.v8technology.hdsc.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleWomanService implements IScheduleService {

    private static LocalDateTime dateTime;
    private static String productType;

    public ScheduleWomanService() {
    }

    public ScheduleWomanService(LocalDateTime dateTime, String productType) {
        this.dateTime = dateTime;
        this.productType = productType;
        checkingScheduleAvailability(this.dateTime, this.productType);
    }

    @Override
    public void checkingScheduleAvailability(LocalDateTime dateTime, String productType) {

        System.out.println("Verificando a disponibilidade de horário...");
        System.out.println("Horário: " + dateTime + " disponível");
        System.out.println(productType + " agendado para as " + dateTime);

        ///
    }
}
