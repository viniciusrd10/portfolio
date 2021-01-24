package br.com.v8technology.hdsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.v8technology.hdsc")
public class HdscApplication {

    public static void main(String[] args) {
        SpringApplication.run(HdscApplication.class, args);
    }

}
