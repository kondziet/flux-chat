package pl.kondziet.springbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kondziet.springbackend.exception.UserAlreadyExistsException;
import pl.kondziet.springbackend.model.dto.LoginRequest;
import pl.kondziet.springbackend.model.dto.LoginResponse;
import pl.kondziet.springbackend.model.dto.RegisterRequest;
import pl.kondziet.springbackend.model.dto.RegisterResponse;
import pl.kondziet.springbackend.service.AuthenticationService;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {

        return authenticationService.authenticate(loginRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(
                        BadCredentialsException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build())
                );
    }

    @PostMapping("/register")
    Mono<ResponseEntity<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {

        return authenticationService.register(registerRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(
                        UserAlreadyExistsException.class,
                        e -> Mono.just(
                                ResponseEntity
                                        .status(HttpStatus.CONFLICT)
                                        .body(
                                                RegisterResponse.builder()
                                                        .message(e.getMessage())
                                                        .build()
                                        )
                        )
                );
    }
}
