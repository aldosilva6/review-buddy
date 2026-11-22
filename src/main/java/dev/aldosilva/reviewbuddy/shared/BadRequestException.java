package dev.aldosilva.reviewbuddy.shared;

/**
 * Created by aldo on Nov, 2026
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
