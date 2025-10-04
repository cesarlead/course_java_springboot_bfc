package com.cesarlead.demo.service;

import com.cesarlead.demo.repository.GreetingRepository;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }


    public String getGreeting() {
        String data = greetingRepository.getGreetingData();
        return data + ", ¡ensamblado desde el servicio!";
    }
}
