package ru.luckyskeet.rolemanagment.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Role Manager API Documentation",
        version = "1.0",
        description = "API documentation for Role Manager module"))
public class SwaggerConfig {
}
