package pl.kondziet.springbackend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.exception.UserAlreadyExistsException;
import pl.kondziet.springbackend.jwt.JwtService;
import pl.kondziet.springbackend.model.dto.LoginRequest;
import pl.kondziet.springbackend.model.dto.LoginResponse;
import pl.kondziet.springbackend.model.dto.RegisterRequest;
import pl.kondziet.springbackend.model.dto.RegisterResponse;
import pl.kondziet.springbackend.model.entity.User;
import pl.kondziet.springbackend.service.AuthenticationService;
import pl.kondziet.springbackend.service.UserService;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ReactiveUserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    public Mono<LoginResponse> authenticate(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        return authenticationManager.authenticate(authentication)
                .flatMap(auth -> userDetailsService.findByUsername(auth.getName())
                        .flatMap(userDetails -> jwtService.generateToken(userDetails)
                                .map(token -> LoginResponse.builder()
                                        .token(token)
                                        .build()))
                );
    }

    @Override
    public Mono<RegisterResponse> register(RegisterRequest registerRequest) {

        Mono<Boolean> emailExists = userService.doesUserWithEmailExists(registerRequest.getEmail());
        Mono<Boolean> usernameExists = userService.doesUserWithUsernameExists(registerRequest.getUsername());

        return Mono.zip(emailExists, usernameExists)
                .map(tuple -> !tuple.getT1() && !tuple.getT2())
                .flatMap(isValid -> {
                    if (isValid) {
                        return userService.saveUser(
                                User.builder()
                                        .email(registerRequest.getEmail())
                                        .username(registerRequest.getUsername())
                                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                                        .visibleNickname(registerRequest.getUsername())
                                        .build()
                        );
                    } else {
                        return Mono.error(new UserAlreadyExistsException("User already exists"));
                    }
                })
                .then(Mono.just(
                        RegisterResponse.builder()
                                .message("User registered successfully")
                                .build()
                ));
    }
}
