package com.harvestlink.marketplace.config;

import java.util.Arrays;

import com.harvestlink.marketplace.auth.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.util.StringUtils;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Value("${app.security.jwt.jwk-set-uri:}") String jwkSetUri) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                "/api/v1/catalog/**",
                                "/api/v1/farmers/**",
                                "/api/v1/sellers/**",
                                "/api/v1/orders/**",
                                "/api/v1/shipments/**"))
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                                    "/api/v1/auth/csrf",
                                    "/api/v1/auth/roles",
                                    "/api/v1/auth/register",
                                    "/api/v1/auth/login",
                                    // The container forwards error responses here; denying it
                                    // would rewrite every 404/500 into an opaque 403.
                                    "/error")
                            .permitAll();
                    authorize.requestMatchers("/api/v1/auth/**").authenticated();
                    authorize.requestMatchers("/api/v1/operator/**").hasRole("OPERATOR");
                    authorize.requestMatchers(
                                    "/api/v1/health",
                                    "/api/v1/catalog",
                                    "/api/v1/catalog/**",
                                    "/api/v1/farmers",
                                    "/api/v1/farmers/**",
                                    "/api/v1/sellers",
                                    "/api/v1/sellers/**",
                                    "/api/v1/orders",
                                    "/api/v1/orders/**",
                                    "/api/v1/shipments",
                                    "/api/v1/shipments/**",
                                    "/actuator/health",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html")
                            .permitAll();
                    if (StringUtils.hasText(jwkSetUri)) {
                        authorize.requestMatchers("/api/**").authenticated();
                    } else {
                        authorize.requestMatchers("/api/**").denyAll();
                    }
                    authorize.anyRequest().denyAll();
                });

        if (StringUtils.hasText(jwkSetUri)) {
            var jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
            http.oauth2ResourceServer(resourceServer ->
                    resourceServer.jwt(jwt -> jwt.decoder(jwtDecoder)));
        }

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(UserAccountRepository accountRepository) {
        return loginId -> accountRepository.findByLoginIdIgnoreCase(loginId)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                        "Account not found"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:5174}") String allowedOrigins) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
