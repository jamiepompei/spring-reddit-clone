package com.springredditclone.security;

import com.springredditclone.exceptions.SpringRedditException;
import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;

@Service

public class JwtProvider {
    private KeyStore keyStore;

    @PostConstruct
    public void init() throws SpringRedditException {
        try {
            keyStore = keyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springreddit.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SpringRedditException(("Exception occurred while loading keystore"));
        }
    }

    public String generateToken(Authentication authentication) throws SpringRedditException {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws SpringRedditException {
        try{
            return (PrivateKey) keyStore.getKey("springreddit", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore", e);
        }
    }

    public boolean validateToken(String jwt) throws SpringRedditException {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() throws SpringRedditException {
        try{
            return keyStore.getCertificate("springreddit").getPublicKey();
        } catch (KeyStoreException e){
            throw new SpringRedditException("Exception occurred while " +
                    "retrieving public key from keystore");
        }
    }

    public String getUsernameFromJwt(String token) throws SpringRedditException {
        Claims claims = parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
