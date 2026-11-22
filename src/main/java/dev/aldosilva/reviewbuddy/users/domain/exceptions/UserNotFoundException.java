package dev.aldosilva.reviewbuddy.users.domain.exceptions;

/**
 * Created by aldo on Nov, 2026
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
