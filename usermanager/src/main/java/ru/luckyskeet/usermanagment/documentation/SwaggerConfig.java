package ru.luckyskeet.usermanagment.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "User Manager API Documentation",
        version = "1.0",
        description = "API documentation for User Manager module"))
public class SwaggerConfig {
}
