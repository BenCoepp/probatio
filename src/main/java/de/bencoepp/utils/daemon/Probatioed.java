package de.bencoepp.utils.daemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Probatioed {
    public static void main(String[] args) {
        SpringApplication.run(Probatioed.class, args);
    }
}
