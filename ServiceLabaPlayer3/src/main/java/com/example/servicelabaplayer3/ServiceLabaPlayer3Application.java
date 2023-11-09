package com.example.servicelabaplayer3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@SpringBootApplication
public class ServiceLabaPlayer3Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceLabaPlayer3Application.class, args);
    }

}
