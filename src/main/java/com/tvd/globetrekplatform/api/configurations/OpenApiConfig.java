package com.tvd.globetrekplatform.api.configurations;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Globetrek Platform API Documentation", version = "1.0"))
public class OpenApiConfig {
}