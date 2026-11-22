package dev.aldosilva.reviewbuddy.config;

import dev.aldosilva.reviewbuddy.users.UsersAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Created by aldo on Nov, 2026
 */
@Configuration
@EnableJpaAuditing
public class PersistenceConfig {

    @Bean
    AuditorAware<Long> auditorProvider(UsersAPI usersAPI) {
        return usersAPI::getCurrentUserId;
    }
}
