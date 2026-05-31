package tz.tante.reporting.manager.configs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilterChainConfig
{
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
  {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(cors -> {})
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(ex -> ex
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
      )
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/v1/auth/**",
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/v3/api-docs/**"
        ).permitAll()
        .requestMatchers(HttpMethod.POST, "/v1/users/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/v1/users/*").permitAll()
        .anyRequest().authenticated()
      );

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties)
  {
    CorsConfiguration configuration = new CorsConfiguration();

    //Allowed Origins
    configuration.setAllowCredentials(corsProperties.isAllowCredentials());
    configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
    configuration.setAllowedMethods(corsProperties.getAllowedMethods());

    //Headers
    configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
    configuration.setExposedHeaders(corsProperties.getExposedHeaders());

    //Map url with configurations
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
