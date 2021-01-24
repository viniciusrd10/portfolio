package br.com.v8technology.hdsc.controllers;

import br.com.v8technology.hdsc.services.IScheduleService;
import br.com.v8technology.hdsc.services.factories.ScheduleServiceFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@OpenAPIDefinition(
        info = @Info(
                title = "API Agenda Barbearia e Salão de Beleza",
                description = "Documentação para integração de agendamento dos serviços disponibilizados",
                termsOfService = "http://swagger.io/terms/",
                license = @License(
                        name = "Apache 2.0",
                        url = "http://springdoc.org"),
                version = "0.0.1"
        ))
@Tag(name = "Agenda", description = "API para agendamento")
@RestController
@RequestMapping("agenda")
public class ScheduleController {

    @GetMapping(name = "/disponibilidade/{data}")
    public IScheduleService checkingScheduleAvailability(LocalDateTime dateTime, String productType) {
        System.out.println("Iniciando Controller");
        return new ScheduleServiceFactory().scheduleServiceFactory(dateTime, productType);

    }
}
