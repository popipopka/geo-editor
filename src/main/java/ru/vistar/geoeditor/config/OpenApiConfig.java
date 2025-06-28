package ru.vistar.geoeditor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Geo Editor API",
                description = "API для взаимодействия с серверной частью приложения Geo Editor",
                version = "1.0.0",
                contact = @Contact(
                        name = "Egor Belykh",
                        email = "eg.belykh@yandex.ru",
                        url = "https://t.me/popipopich"
                )
        )
)
@Configuration
public class OpenApiConfig {
}