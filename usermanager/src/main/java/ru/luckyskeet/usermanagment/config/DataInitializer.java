package ru.luckyskeet.usermanagment.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.luckyskeet.usermanagment.model.User;
import ru.luckyskeet.usermanagment.repository.UserRepository;
import ru.luckyskeet.usermanagment.security.UserAccessLevel;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        createAdminUser();
    }

    private void createAdminUser() {
        User admin = new User()
                .setName("admin")
                .setPassword(encoder.encode("admin"))
                .setEmail("aDukeFan@yandex.com")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setAccessLevel(UserAccessLevel.ADMIN);
        repository.save(admin);
    }
}
