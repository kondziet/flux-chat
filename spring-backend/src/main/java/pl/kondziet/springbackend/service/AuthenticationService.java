package pl.kondziet.springbackend.service;

import pl.kondziet.springbackend.model.dto.LoginRequest;
import pl.kondziet.springbackend.model.dto.LoginResponse;
import pl.kondziet.springbackend.model.dto.RegisterRequest;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

    Mono<LoginResponse> authenticate(LoginRequest loginRequest);
    Mono<Void> register(RegisterRequest registerRequest);
}
