package com.dvc.RtLogistics.utils;


import com.dvc.RtLogistics.entity.UserRole;
import com.dvc.RtLogistics.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserRepository userRepository;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() +  1000*60*15))
                .signWith(getSigningKey()).compact();
    }

    public String getUserNameFromToken(String token){
        String val = extractClaim(token,(claims)-> claims.getSubject());
        return val;
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException e){
            throw new RuntimeException("The jwt token is expired! please retry again");
        }
        catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private boolean tokentExpired(String token){
        return extractClaim(token,(claims)->claims.getExpiration()).before(new Date());
    }

    public boolean validateToken(UserDetails userDetails, String token){
        return getUserNameFromToken(token).equals(userDetails.getUsername()) && !tokentExpired(token);
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userRepository.findByUserName(username).get()
                .getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toList());

        claims.put("roles", roles);
        return Jwts.builder().setSubject(username)
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +1000*60*30))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}
