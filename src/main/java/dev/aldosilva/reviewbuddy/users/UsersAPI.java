package dev.aldosilva.reviewbuddy.users;

import dev.aldosilva.reviewbuddy.users.config.AuthUtils;
import dev.aldosilva.reviewbuddy.users.domain.UserService;
import dev.aldosilva.reviewbuddy.users.domain.models.UserDto;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Created by aldo on Nov, 2026
 */
@Component
public class UsersAPI {

    private final UserService userService;

    UsersAPI(UserService userService) {
        this.userService = userService;
    }

    public Optional<UserDto> findById(Long id) {
        return userService.findById(id);
    }

    public Optional<String> getCurrentUserName() {
        return getCurrentUserId().flatMap(userService::findById).map(UserDto::name);
    }

    public Optional<Long> getCurrentUserId() {
        return Optional.ofNullable(AuthUtils.getCurrentUserId());
    }
}
