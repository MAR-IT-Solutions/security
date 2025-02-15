package com.mar.solutions.security.jwt;


import com.mar.solutions.security.dto.UserDTO;
import com.mar.solutions.security.entity.UserEntity;
import com.mar.solutions.security.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

//    @Value("${jwt.secret:secretV908790234-32795345734323409984bvdssnd94wqeualue}")
    private final String secret = getDynamicSecret();

    @Value("${jwt.expiration:360}")
    private long expiration;

    @Autowired
    private UserRepository userRepository;

    private String getDynamicSecret(){
        log.debug("Generating dynamic secret");
        SecureRandom random = new SecureRandom();
        byte[] values = new byte[32];
        random.nextBytes(values);
        return new String(values);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        log.info("Generating token for user: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        Optional<UserEntity> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isPresent()){
            claims.put("clientCode", user.get().getClientCode());
            claims.put("email", user.get().getEmail());
            claims.put("name", user.get().getName());
            String clientCategory;
            if (StringUtils.isEmpty(user.get().getClientCode())){
                clientCategory = "ADMIN";
            } else {
                clientCategory = "CLIENT";
            }
            claims.put("roles", user.get().getRoles().stream()
                    .map(role ->
                            "ROLE_"+clientCategory+"_"+role.getRole()
                    )
                    .toList());
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}

