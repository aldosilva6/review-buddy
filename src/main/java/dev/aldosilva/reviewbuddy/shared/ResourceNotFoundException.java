package dev.aldosilva.reviewbuddy.shared;

/**
 * Created by aldo on Nov, 2026
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
