package com.absencia.diginamic.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.SslBundleProperties.Key;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.absencia.diginamic.constants.JWTConstants;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenUtil implements Serializable {

    // @Value("${jwt.secret}")
    // private String secretKey;

    public String getEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Object usernameObject = claims.get("username");
        return usernameObject != null ? usernameObject.toString() : null;
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = JWTConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(JWTConstants.SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(final String email) {
        return Jwts
                .builder()
                // .subject(email)
                .claim("username", email)
                // TODO: Store roles in JWT
                // .issuer("admin")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + JWTConstants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        if (username.equals(userDetails.getUsername()) && !isTokenExpired(token)
                && verifyTokenSignature(token, getSecretKey())) {
            return true;
        }
        return false;
    }

    private Boolean verifyTokenSignature(String token, SecretKey secret) {
        try {
            Jwts.parser().verifyWith(secret).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}