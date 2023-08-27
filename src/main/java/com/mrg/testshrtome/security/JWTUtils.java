package com.mrg.testshrtome.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

public class JWTUtils {

    private static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(JWTConstants.JWT_SECRET),
            SignatureAlgorithm.HS512.getJcaName());

    public static String generateJWTUserToken(String userName) {

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWTConstants.EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(hmacKey)
                .compact();
        return token;
    }
    public static String generateJWTUserToken(String userName, Map<String,Object> claims) {

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWTConstants.EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .addClaims(claims)
                .signWith(hmacKey)
                .compact();
        return token;
    }

    public static String getUsernameFromJWTUserToken(String token) {

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getSubject();
    }

    public static Map<String,Object> getTokenClaims(String token) {

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);

        return claims.getBody();
    }

    public static Object getClaim(String token,String claim) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().get(claim);
    }

    public static ArrayList<GrantedAuthority> getRoles(String token)
    {
        ArrayList<String> roles = (ArrayList<String>) getClaim(token, "roles");
        ArrayList<GrantedAuthority> authorities =new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }
    public static boolean isJWTTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    public static String[] decodedBase64(String token) {

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String pairedCredentials = new String(decodedBytes);
        String[] credentials = pairedCredentials.split(":", 2);

        return credentials;

    }

    public static void main(String[] args) {
        Map<String,Object> claims =new HashMap<>();
        List list=new ArrayList();
        list.add("ABD");
        claims.put("Name",list);
        System.out.println(JWTUtils.generateJWTUserToken("MRG",claims));
    }
}
