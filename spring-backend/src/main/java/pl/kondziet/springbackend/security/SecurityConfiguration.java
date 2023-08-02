package pl.kondziet.springbackend.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import pl.kondziet.springbackend.jwt.JwtVerifyFilter;

import java.util.Arrays;

@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final JwtVerifyFilter jwtVerifyFilter;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        httpSecurity.cors().configurationSource(request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
            corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
            corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        });
        httpSecurity.csrf().disable();
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();

        httpSecurity.authorizeExchange(request -> request
                .pathMatchers("/api/authentication/**")
                .permitAll()
                .anyExchange()
                .authenticated()
        );

        httpSecurity.addFilterAt(jwtVerifyFilter, SecurityWebFiltersOrder.CORS);

        return httpSecurity.build();
    }
}
