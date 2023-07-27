package pl.kondziet.springbackend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUserEmail(String token);
    Date extractTokenExpiration(String token);
    String generateToken(UserDetails userDetails);
}
