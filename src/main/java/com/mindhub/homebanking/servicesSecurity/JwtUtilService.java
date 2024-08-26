package com.mindhub.homebanking.servicesSecurity;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service//Servicio para generar el token firmado con la clave secreta.
public class JwtUtilService {

    private static  final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    private static  final long EXPIRATION_TOKEN = 1000 * 60 * 60;

    //Este metodo va a verificar un token usando una clave secreta y luego va a extraer y devolver las claims del token verificado.
    public Claims extractAllClaims(String token) { //Claims = lo que se detalla en el payload, este metodo extrae todos los claims.
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    //Este metodo sirve para devolver un claim en particular pero al tener <T> es una clase generica y por lo tanto no se que me puede devovler
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String createToken(Map<String, Object> claims , String username) {

        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();//Estructura para asociar una clave a un valor.
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        return createToken(claims, userDetails.getUsername());
    }

}
