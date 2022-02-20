package com.maknaeontop.communication.jwt;

/**
 * @author Namho Kim
 * @since 1.0
 * @version 1.0
 */

import com.maknaeontop.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = 1845785136697313752L;
    public static final long JWT_TOKEN_VALIDITY = 2512 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Method to get user id from jwt token.
     *
     * @param token the jwt token
     * @return      the user id
     */
    public String getIdByToken(String token) {
        Claims claims = getClaimByToken(token);
        return claims.getSubject();
    }

    /**
     * Method to get expiration date from jwt token.
     *
     * @param token the jwt token
     * @return      the expiration date
     */
    public Date getExpirationByToken(String token) {
        Claims claims = getClaimByToken(token);
        return claims.getExpiration();
    }

    /**
     * Method to get claims from token.
     *
     * @param token the jwt token
     * @return      the claims
     */
    private Claims getClaimByToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Method to check if the token has expired.
     *
     * @param token the jwt token
     * @return      whether token has expired
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationByToken(token);
        return expiration.before(new Date());
    }

    /**
     * Method to generate token.
     *
     * @param user  user information including user id
     * @return      generated token
     */
    public String generateToken(User user) {
        return createToken(user.getId());
    }

    /**
     * Method to create token.
     *
     * @param subject   the user id
     * @return          created token
     */
    private String createToken(String subject) {
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

    /**
     * Method to validate token.
     *
     * @param token the jwt token
     * @return      false if token is expired, true if token is not expired
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Method to get id from the token.
     *
     * @param request   HttpServletRequest to get token
     * @return          the user id
     */
    public String getIdFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String userId = null;

        // 헤더가 존재하고 Bearer로 시작하면 username 추출
        if (header != null && header.startsWith("Bearer ")) {
            userId = getIdByToken(header.substring(7));
        }
        return userId;
    }
}