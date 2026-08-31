package com.randombank.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class OnboardingApplication {
    @GetMapping
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(OnboardingApplication.class, args);
    }
}
