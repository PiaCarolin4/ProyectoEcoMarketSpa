package cl.duocuc.inventoryservice.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenApiCustomizer hateoasCustomizer() {
        return openApi -> {
            if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
                openApi.getComponents().getSchemas().values().forEach(schema -> {
                    if (schema.getProperties() != null) {
                        schema.getProperties().remove("_links");
                    }
                });
            }
        };
    }
}

