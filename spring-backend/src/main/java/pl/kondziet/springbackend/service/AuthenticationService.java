package pl.kondziet.springbackend.service;

import pl.kondziet.springbackend.model.dto.LoginRequest;
import pl.kondziet.springbackend.model.dto.LoginResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

    Mono<LoginResponse> authenticate(LoginRequest loginRequest);
}
