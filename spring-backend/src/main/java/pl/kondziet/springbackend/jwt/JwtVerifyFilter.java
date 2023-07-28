package pl.kondziet.springbackend.jwt;

import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pl.kondziet.springbackend.repository.UserRepository;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Order(1)
@Component
public class JwtVerifyFilter implements WebFilter {

    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        System.out.println("REQUEST ARRIVED");

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(token);

            if (StringUtils.hasText(userEmail)) {
                return userRepository.findByEmail(userEmail)
                        .map(userDetails -> jwtService.isTokenValid(token, userDetails))
                        .flatMap(isValid -> {
                           if (isValid) {
                               System.out.printf("Bearer token: %s Email: %s\n", token, userEmail);
                               Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, null);
                               return ReactiveSecurityContextHolder.getContext()
                                       .doOnNext(securityContext -> securityContext.setAuthentication(authentication))
                                       .then(chain.filter(exchange));
                           } else {
                               return Mono.empty();
                           }
                        });
            }
        }

        return chain.filter(exchange);
    }
}
