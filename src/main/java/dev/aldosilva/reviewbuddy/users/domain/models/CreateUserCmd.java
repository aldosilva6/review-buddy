package dev.aldosilva.reviewbuddy.users.domain.models;

/**
 * Created by aldo on Nov, 2026
 */
public record CreateUserCmd(String name, String email, String password, Role role) {}
