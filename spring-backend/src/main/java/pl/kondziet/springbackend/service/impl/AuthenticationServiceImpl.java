package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.exception.UserAlreadyExistsException;
import pl.kondziet.springbackend.jwt.JwtService;
import pl.kondziet.springbackend.model.dto.LoginRequest;
import pl.kondziet.springbackend.model.dto.LoginResponse;
import pl.kondziet.springbackend.model.dto.RegisterRequest;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.service.AuthenticationService;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<LoginResponse> authenticate(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        return authenticationManager.authenticate(authentication)
                .flatMap(auth -> userDetailsService.findByUsername(loginRequest.getEmail())
                        .map(userDetails -> {
                            String token = jwtService.generateToken(userDetails);
                            return LoginResponse.builder()
                                    .token(token)
                                    .build();
                        })
                );
    }

    @Override
    public Mono<Void> register(RegisterRequest registerRequest) {
        return userDetailsService.findByUsername(registerRequest.getEmail())
                .flatMap(existingUser -> {
                    return Mono.error(new UserAlreadyExistsException("User with the same email already exists."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    User user = User.builder()
                            .nickName(registerRequest.getNickName())
                            .email(registerRequest.getEmail())
                            .password(registerRequest.getPassword())
                            .build();
//                    return userDetailsService.save(user);
                    return null;
                }))
                .then();
    }
}
