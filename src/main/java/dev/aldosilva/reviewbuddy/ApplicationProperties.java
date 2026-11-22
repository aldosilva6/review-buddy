package dev.aldosilva.reviewbuddy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Created by aldo on Nov, 2026
 */
@ConfigurationProperties(prefix = "app")
@Validated
public record ApplicationProperties() {}
