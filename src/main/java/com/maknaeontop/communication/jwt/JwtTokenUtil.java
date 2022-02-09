package com.maknaeontop.communication.jwt;

import com.maknaeontop.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    //private static final long serialVersionUID = -2550185165626007488L;
    private static final long serialVersionUID = 1845785136697313752L;
    public static final long JWT_TOKEN_VALIDITY = 2512 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getIdByToken(String token) {
        Claims claims = getClaimByToken(token);
        return claims.getSubject();
    }

    //retrieve expiration date from jwt token
    public Date getExpirationByToken(String token) {
        Claims claims = getClaimByToken(token);
        return claims.getExpiration();
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getClaimByToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationByToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(User user) {
        return doGenerateToken(user.getId());
    }

    //while creating the token -
//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//2. Sign the JWT using the HS512 algorithm and secret key.
//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   compaction of the JWT to a URL-safe string
    private String doGenerateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date(System.currentTimeMillis());
        Date validity = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}