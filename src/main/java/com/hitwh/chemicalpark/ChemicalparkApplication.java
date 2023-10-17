package com.hitwh.chemicalpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@CrossOrigin(origins = "*")
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ChemicalparkApplication {
        public static void main(String[] args) {
        SpringApplication.run(ChemicalparkApplication.class, args);
    }

}
