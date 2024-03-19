package com.absencia.diginamic.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.absencia.diginamic.Model.User;
import com.absencia.diginamic.constants.JWTConstants;

import java.io.Serializable;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenUtil implements Serializable {

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts
            .parser()
            .verifyWith(new SecretKeySpec(JWTConstants.SIGNING_KEY.getBytes(), "HmacSHA256"))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(final String email) {
        final Claims claims = Jwts
            .claims()
            .subject(email)
            .build();

        // TODO remove "a" from "scoapes"?
        // claims.put("scoapes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // String secret = JWTConstants.SIGNING_KEY;
        // byte[] secretBytes = secret.getBytes();
        // Key key = new SecretKeySpec(secretBytes, "HmacSHA256");

        return Jwts.builder()
            .claims(claims)
            .issuer("admin")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + JWTConstants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
            // TODO find a way to sign with key
            // .signWith(key)
            .signWith(SignatureAlgorithm.HS256, JWTConstants.SIGNING_KEY)
            .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

}