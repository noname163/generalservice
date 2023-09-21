package com.cepa.generalservice.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.entities.UserInformation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expires-time}")
    private long expireTime;

    private String doGenerateToken(Map<String, Object> claims, String subject, Integer expriesTime) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + expireTime * expriesTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String generateJwtToken(UserInformation user, Integer expiresTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return doGenerateToken(claims, user.getEmail(), expiresTime);
    }

    public Jws<Claims> getJwsClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    }

    public String getEmailFromClaims(Claims claims) {
        return claims.get("email").toString();
    }

    public String getPhoneFromClaims(Claims claims) {
        return claims.get("phone").toString();
    }
}
