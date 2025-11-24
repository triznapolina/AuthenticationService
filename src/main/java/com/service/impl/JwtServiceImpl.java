package com.service.impl;


import com.entity.User;
import com.service.JwtService;
import com.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${security.token.signing.key}")
    private String jwtSigningKey;

    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;


    private final UserService userService;

    @Override
    public String generateAccessToken(UserDetails user) {
        Map<String, Object> claims = createClaims(user);
        return generateToken(claims, user, accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        Map<String, Object> claims = createClaims(user);
        return generateToken(claims, user, refreshTokenExpiration);
    }

    @Override
    public Map<String, Object> createClaims(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        if (user instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("role", customUserDetails.getRole());
        }
        return claims;
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails user, long expiryTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSigningKey())
                .compact();
    }


    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }



    @Override
    public boolean validateAccessToken(@NonNull String accessToken) {
        String username = extractUserName(accessToken);
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
        return isTokenValid(accessToken, userDetails);
    }

    @Override
    public boolean validateRefreshToken(@NonNull String refreshToken) {
        String username = extractUserName(refreshToken);
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
        return isTokenValid(refreshToken, userDetails);
    }


    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }


    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    @Override
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }



}


