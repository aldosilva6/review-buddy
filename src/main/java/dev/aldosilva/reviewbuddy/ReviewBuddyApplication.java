package dev.aldosilva.reviewbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReviewBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewBuddyApplication.class, args);
    }
}
