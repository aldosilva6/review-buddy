package dev.aldosilva.reviewbuddy.config;

import dev.aldosilva.reviewbuddy.ApplicationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by aldo on Nov, 2026
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ApplicationProperties props;

    WebMvcConfig(ApplicationProperties props) {
        this.props = props;
    }
}
