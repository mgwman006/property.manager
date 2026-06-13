package tz.tante.property.manager.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
      .group("property-manager")
      .packagesToScan("tz.tante.property.manager.controllers")
      .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .components(new Components()
        .addSecuritySchemes("bearerAuth",
          new SecurityScheme()
            .name("bearerAuth")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        )
      );
  }
}
