package com.absencia.diginamic.config;

import io.jsonwebtoken.Claims;
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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String secretKey;

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
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
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
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && verifyTokenSignature(token, getPublicKey()));
    }

    private Boolean verifyTokenSignature(String token, PublicKey publicKey) {
        try {
            Jwts.parser().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts
                .parser()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    private static PublicKey getPublicKey() {
        String publicKeyString = JWTConstants.PUBLIC_KEY.replaceAll("\\n", "");
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            return keyFactory.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}