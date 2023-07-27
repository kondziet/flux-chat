package pl.kondziet.springbackend.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import pl.kondziet.springbackend.jwt.JwtVerifyFilter;

@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final JwtVerifyFilter jwtVerifyFilter;
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        httpSecurity.cors(Customizer.withDefaults());
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.csrf().disable();

        httpSecurity.authorizeExchange(request -> request
                .anyExchange()
                .permitAll()
        );

        httpSecurity.addFilterAt(jwtVerifyFilter, SecurityWebFiltersOrder.CORS);

        return httpSecurity.build();
    }
}
