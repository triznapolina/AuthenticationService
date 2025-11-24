package com.service;

import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(UserDetails user);


    String generateRefreshToken(UserDetails user);

    Map<String, Object> createClaims(UserDetails user);

    String generateToken(Map<String, Object> extraClaims, UserDetails user, long expiryTime);


    String extractUserName(String token);

    boolean validateAccessToken(@NonNull String accessToken);


    boolean validateRefreshToken(@NonNull String refreshToken);


    boolean isTokenValid(String token, UserDetails userDetails);


   <T> T extractClaim(String token, Function<Claims, T> claimsResolvers);


    boolean isTokenExpired(String token);


    Date extractExpiration(String token);


    Claims extractAllClaims(String token);


    Key getSigningKey();

    String extractRole(String token);

}
