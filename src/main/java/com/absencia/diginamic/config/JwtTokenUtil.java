package com.absencia.diginamic.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.absencia.diginamic.constants.JWTConstants;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


@Component
public class JwtTokenUtil implements Serializable {

    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).get("username").toString();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private static PrivateKey getPrivateKey() {
        byte[] keyBytes = Base64.getDecoder().decode(JWTConstants.SIGNING_KEY);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            return keyFactory.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static SecretKey getSecretKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(JWTConstants.SIGNING_KEY), SignatureAlgorithm.RS256.getJcaName());
    }

    public String generateToken(final String email) {
        return Jwts
            .builder()
            .subject(email)
            // .claim("username", email)
            // TODO: Store roles in JWT
            // .issuer("admin")
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + JWTConstants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
            .signWith(getPrivateKey())
            .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts
            .parser()
            // .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}