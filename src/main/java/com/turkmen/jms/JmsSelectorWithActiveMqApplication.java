package com.turkmen.jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JmsSelectorWithActiveMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsSelectorWithActiveMqApplication.class, args);
    }
}
