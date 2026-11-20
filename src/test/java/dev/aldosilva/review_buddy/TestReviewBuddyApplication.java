package dev.aldosilva.review_buddy;

import org.springframework.boot.SpringApplication;

public class TestReviewBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReviewBuddyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
