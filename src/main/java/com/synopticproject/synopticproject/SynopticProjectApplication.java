package com.synopticproject.synopticproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class SynopticProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynopticProjectApplication.class, args);
    }

}
