package br.com.v8technology.hdsc.services.factories;

import br.com.v8technology.hdsc.services.IScheduleService;
import br.com.v8technology.hdsc.services.ScheduleManService;
import br.com.v8technology.hdsc.services.ScheduleWomanService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleServiceFactory {

    IScheduleService service;

    public IScheduleService scheduleServiceFactory(LocalDateTime dateTime, String productType) {
        System.out.println("Iniciando Factory");

        switch (productType){
            case "CM":
                System.out.println("Corte Masculino");
               service = new ScheduleManService(dateTime, productType);
               break;
            case "CW":
                System.out.println("Corte feminino");
                service = new ScheduleWomanService(dateTime, productType);
                break;
            default:
                System.out.println("Informe o tipo de serviço.");
        }

        return service;
    }

}
