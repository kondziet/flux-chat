package pl.kondziet.springbackend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    Mono<Boolean> isTokenValid(String token, UserDetails userDetails);
    Mono<String> extractUserEmail(String token);
    Mono<Date> extractTokenExpiration(String token);
    Mono<String> generateToken(UserDetails userDetails);
}
