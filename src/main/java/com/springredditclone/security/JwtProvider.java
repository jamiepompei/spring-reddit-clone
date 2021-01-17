package com.springredditclone.security;

import com.springredditclone.exceptions.SpringRedditException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

@Service
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

//    @PostConstruct
//    public void init() throws SpringRedditException {
//        try {
//            keyStore = keyStore.getInstance("JKS");
//            InputStream resourceAsStream = getClass().getResourceAsStream("/springreddit.jks");
//            keyStore.load(resourceAsStream, "secret".toCharArray());
//        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
//            throw new SpringRedditException("Exception occurred while loading keystore", e);
//        }
//    }

    public String generateToken(Authentication authentication) throws SpringRedditException {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String username) throws SpringRedditException {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

//    private PrivateKey getPrivateKey() throws SpringRedditException {
//        try{
//            return (PrivateKey) keyStore.getKey("springreddit", "secret".toCharArray());
//        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
//            throw new SpringRedditException("Exception occurred while retrieving public key from keystore", e);
//        }
//    }

    public boolean validateToken(String jwt) {
        try{
            parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
                return true;
        } catch(MalformedJwtException e){
            log.error("Invalid JWT Token: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

//    private PublicKey getPublicKey() throws SpringRedditException {
//        try{
//            return keyStore.getCertificate("springreddit").getPublicKey();
//        } catch (KeyStoreException e){
//            throw new SpringRedditException("Exception occurred while " +
//                    "retrieving public key from keystore", e);
//        }
//    }

    public String getUsernameFromJwt(String token) throws SpringRedditException {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis(){
        return jwtExpirationInMillis;
    }


}
