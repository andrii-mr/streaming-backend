package conbo.ai.streamingbackend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI ingestionOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Ingestion of an edge computing device API").version("1.0"));
  }
}
