package com.team5z.projectAuth.global.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("Auth API")
                .version("1.0.0")
                .description("Auth API Documentation")
                ;

        return new OpenAPI()
                .info(info);
    }
}
