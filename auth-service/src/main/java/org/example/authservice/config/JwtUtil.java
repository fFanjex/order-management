package org.example.authservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.example.authservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessTokenValidity;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId().toString())
                .withClaim("roles", new ArrayList<>(user.getRoles()))
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidity))
                .sign(algorithm);
    }

    public boolean isValidToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        String username = jwt.getSubject();
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        return new UsernamePasswordAuthenticationToken(username,
                null,
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }

    public String getUsernameFromToken(String token) {
        return JWT.require(algorithm).build().verify(token).getSubject();
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(JWT.require(algorithm).build().verify(token).getClaim("userId").asString());
    }
}
