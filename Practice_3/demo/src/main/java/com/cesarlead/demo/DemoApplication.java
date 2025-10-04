package com.cesarlead.demo;


import com.cesarlead.demo.service.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private GreetingService greetingService;

    public DemoApplication(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 3. Usamos el servicio que Spring nos "inyectó"
        System.out.println(greetingService.getGreeting());
    }


}
