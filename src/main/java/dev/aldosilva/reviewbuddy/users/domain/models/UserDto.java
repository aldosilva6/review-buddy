package dev.aldosilva.reviewbuddy.users.domain.models;

import java.time.Instant;

/**
 * Created by aldo on Nov, 2026
 */
public record UserDto(Long id, String name, String email, Role role, Instant createdAt) {}
