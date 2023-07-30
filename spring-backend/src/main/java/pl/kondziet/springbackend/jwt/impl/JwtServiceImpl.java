package pl.kondziet.springbackend.jwt.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.kondziet.springbackend.jwt.JwtService;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public Mono<Boolean> isTokenValid(String token, UserDetails userDetails) {
        Mono<Boolean> isExpired = isTokenExpired(token);
        Mono<String> userEmail = extractUserEmail(token);

        return Mono.zip(isExpired, userEmail)
                .map(tuple -> !tuple.getT1() && tuple.getT2().equals(userDetails.getUsername()));
    }

    private Mono<Boolean> isTokenExpired(String token) {
        Mono<Date> expirationDate = extractTokenExpiration(token);
        return expirationDate
                .map(expiration -> expiration.before(new Date(System.currentTimeMillis())));
    }

    public Mono<String> extractUserEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public Mono<Date> extractTokenExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> Mono<T> extractClaim(String token, Function<Claims, T> claimResolver) {
        return extractAllClaims(token)
                .map(claimResolver);
    }

    private Mono<Claims> extractAllClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();

        return Mono.just(
                jwtParser
                        .parseClaimsJws(token)
                        .getBody()
        );
    }

    public Mono<String> generateToken(UserDetails userDetails) {
        return Mono.just(
                Jwts.builder()
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                        .signWith(getSigningKey())
                        .compact()
        );
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
