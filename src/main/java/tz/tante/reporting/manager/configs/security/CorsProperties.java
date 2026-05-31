package tz.tante.reporting.manager.configs.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

  private List<String> allowedOrigins;
  private List<String> allowedMethods;
  private List<String> allowedHeaders;
  private List<String> exposedHeaders;
  private boolean allowCredentials;

}
