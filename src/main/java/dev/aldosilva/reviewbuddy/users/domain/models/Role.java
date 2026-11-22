package dev.aldosilva.reviewbuddy.users.domain.models;

/**
 * Created by aldo on Nov, 2026
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static String getRoleHierarchy() {
        return ROLE_ADMIN.name() + " > " + ROLE_USER.name();
    }
}
