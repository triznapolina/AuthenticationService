package com.service;

import com.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.token.signing.key}")
    private String jwtSigningKey;

    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;


    public String generateAccessToken(UserDetails user) {
        Map<String, Object> claims = createClaims(user);
        return generateToken(claims, user, accessTokenExpiration);
    }


    public String generateRefreshToken(UserDetails user) {
        Map<String, Object> claims = createClaims(user);
        return generateToken(claims, user, refreshTokenExpiration);
    }


    public Map<String, Object> createClaims(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        if (user instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("role", customUserDetails.getRole());
        }
        return claims;
    }



    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails user, long expiryTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSigningKey())
                .compact();
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken);
    }

    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return extractAllClaims(token);
    }

}


