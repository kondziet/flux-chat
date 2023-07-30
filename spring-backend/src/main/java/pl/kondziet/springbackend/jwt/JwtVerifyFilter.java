package pl.kondziet.springbackend.jwt;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Order(1)
@Component
public class JwtVerifyFilter implements WebFilter {

    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            Mono<String> userEmail = jwtService.extractUserEmail(token);
            Mono<UserDetails> userDetails = userEmail.flatMap(userDetailsService::findByUsername);
            Mono<Boolean> isTokenValid = userDetails.flatMap(user -> jwtService.isTokenValid(token, user));

            return isTokenValid
                    .flatMap(isValid -> {
                        if (isValid) {
                            return userDetails.flatMap(user -> {
                                System.out.printf("Bearer token: %s Email: %s\n", token, user.getUsername());
                                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                                return ReactiveSecurityContextHolder.getContext()
                                        .doOnNext(securityContext -> securityContext.setAuthentication(authentication))
                                        .then(chain.filter(exchange));
                            });
                        } else {
                            return Mono.empty();
                        }
                    })
                    .onErrorResume(
                            JwtException.class,
                            e -> {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            }
                    );
        }

        return chain.filter(exchange);
    }
}
