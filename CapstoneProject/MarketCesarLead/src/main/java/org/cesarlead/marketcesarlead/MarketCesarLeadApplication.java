package org.cesarlead.marketcesarlead;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MarketCesarLeadApplication {

    public static void main(String[] args) {

        SpringApplication.run(MarketCesarLeadApplication.class, args);

    }

}
