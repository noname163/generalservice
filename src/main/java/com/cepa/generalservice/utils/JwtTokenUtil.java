package com.cepa.generalservice.utils;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cepa.generalservice.data.constants.Common;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.exceptions.InValidAuthorizationException;
import com.cepa.generalservice.services.userService.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenUtil {
    @Autowired
    private EnvironmentVariables environmentVariables;

    @Autowired
    private UserService userService;

    private String doGenerateToken(Map<String, Object> claims, String subject, Integer expriesTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + environmentVariables.getExpireTime() * expriesTime))
                .signWith(SignatureAlgorithm.HS512, environmentVariables.getJwtSecret()).compact();
    }

    public String generateJwtToken(UserInformation user, Integer expiresTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        claims.put("avatar", user.getImageURL());
        claims.put("fullName", user.getFullName());
        return doGenerateToken(claims, user.getEmail(), expiresTime);
    }

    public Jws<Claims> getJwsClaims(String token, String from) {
        String secretKey = "";
        if (Common.BEARER.equals(from)) {
            secretKey = environmentVariables.getJwtSecret();
        }
        if (Common.SERVICE.equals(from)) {
            secretKey = environmentVariables.getJwtSecretService();
        }
        Jws<Claims> tokenInfor = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        Claims claims = tokenInfor.getBody();

        // Check if the token has expired
        Date expirationDate = claims.getExpiration();
        Date now = new Date();

        if (expirationDate != null && expirationDate.before(now)) {
            throw new InValidAuthorizationException("Token has expired");
        }

        String email = claims.get("email").toString();
        UserInformation userInformation = userService.getUserByEmail(email);

        if (from.equals(Common.BEARER) && !token.equals(userInformation.getAccessToken())
                && !token.equals(userInformation.getRefreshToken())) {
            throw new InValidAuthorizationException("Token not valid");
        }

        return tokenInfor;
    }

    public Jws<Claims> getJwsClaimsForService(String token) {
        Jws<Claims> tokenInfor = Jwts.parser().setSigningKey(environmentVariables.getJwtSecret()).parseClaimsJws(token);
        Claims claims = tokenInfor.getBody();

        // Check if the token has expired
        Date expirationDate = claims.getExpiration();
        Date now = new Date();

        if (expirationDate != null && expirationDate.before(now)) {
            throw new InValidAuthorizationException("Token has expired");
        }

        String email = claims.get("email").toString();
        UserInformation userInformation = userService.getUserByEmail(email);

        if (!token.equals(userInformation.getAccessToken()) && !token.equals(userInformation.getRefreshToken())) {
            throw new InValidAuthorizationException("Token not valid");
        }

        return tokenInfor;
    }

    public Claims verifyRefreshToken(String refreshToken) {
        Jws<Claims> refreshTokenClaims = Jwts.parser().setSigningKey(environmentVariables.getJwtSecret())
                .parseClaimsJws(refreshToken);
        if (refreshTokenClaims.getBody().getExpiration().before(new Date())) {
            throw new InValidAuthorizationException("Refresh token has expired");
        }

        UserInformation userInformation = userService.getUserByEmail(getEmailFromClaims(refreshTokenClaims.getBody()));

        if (!refreshToken.equals(userInformation.getRefreshToken())) {
            throw new InValidAuthorizationException("Token not valid");
        }

        return refreshTokenClaims.getBody();
    }

    public Boolean verifyAccessToken(String accessToken) {
        Jws<Claims> accessTokenClaims = Jwts.parser().setSigningKey(environmentVariables.getJwtSecret())
                .parseClaimsJws(accessToken);
        if (accessTokenClaims.getBody().getExpiration().before(new Date())) {
            throw new InValidAuthorizationException("Refresh token has expired");
        }

        UserInformation userInformation = userService.getUserByEmail(getEmailFromClaims(accessTokenClaims.getBody()));

        if (!accessToken.equals(userInformation.getAccessToken())) {
            throw new InValidAuthorizationException("Token not valid");
        }

        return true;
    }

    public String getEmailFromClaims(Claims claims) {
        return claims.get("email").toString();
    }

    public String getPhoneFromClaims(Claims claims) {
        return claims.get("phone").toString();
    }

    public UserInformation getJwsClaimsForGoogle(String token) {        
        String email = "";
        try {
            DecodedJWT jwt = JWT.decode(token);
            if(!jwt.getIssuer().equals("https://accounts.google.com")|| !jwt.getAudience().get(0).equals("26921650638-a5s7agh9vm66h679a6891cqqq1o4slr4.apps.googleusercontent.com")){
                throw new InValidAuthorizationException("Token from google not valid");
            }
            Date expirationDate = jwt.getExpiresAt();
            Date now = new Date();
            if (expirationDate != null && expirationDate.before(now)) {
                throw new InValidAuthorizationException("Token has expired");
            }
            Claim claim = jwt.getClaim("email");
            email = claim.toString();
        } catch (Exception e) {
            log.error("Google parse token error {}", e.getMessage());
        }    
        UserInformation userInformation = userService.getUserByEmail(email);

        return userInformation;
    }
}
