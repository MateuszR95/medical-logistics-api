package pl.mateusz.medicallogistics.medicallogisticsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        .requestMatchers("/api/set-inspections/perform-inspection").hasAnyRole("ADMIN",
            "WAREHOUSE_CUSTOMER_SERVICE", "WAREHOUSE_OPERATOR")
        .requestMatchers("/api/inbound-files/**").hasAnyRole("ADMIN")
        .requestMatchers("/api/set-instances/**",
          "/api/set-inspections/missing-parts").authenticated()
        .anyRequest().authenticated()

      )
        .httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable
      )
        .headers(headers -> headers
        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));


    return http.build();
  }

  /**
   * Configures the password encoder for the application.
   *
   * @return a {@link PasswordEncoder} instance that can be used to encode passwords
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
