package pl.mateusz.medicallogistics.medicallogisticsapi.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain for the application.
   *
   * @param http the {@link HttpSecurity} to modify
   * @return the {@link SecurityFilterChain} that defines the security configuration
   * @throws Exception if an error occurs while configuring the security filter chain
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()
        .anyRequest().authenticated()
      )
      .csrf(csrf -> csrf
        .ignoringRequestMatchers(
          PathRequest.toH2Console()
        )
      )
      .headers(headers -> headers
        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
      )
        .formLogin(Customizer.withDefaults());
    return http.build();
  }
}
