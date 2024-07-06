 package org.market.ServiceLayer;

 import io.jsonwebtoken.Claims;
 import io.jsonwebtoken.ExpiredJwtException;
 import io.jsonwebtoken.Jwts;
 import io.jsonwebtoken.io.Decoders;
 import io.jsonwebtoken.security.Keys;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.stereotype.Service;

 import java.security.Key;
 import java.util.Date;
 import java.util.function.Function;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long access_expiration;

    @Value("${jwt.refresh.expiration}")
    private long refresh_expiration;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        try{
            return extractClaim(token, Claims::getSubject);
        }catch (ExpiredJwtException e){
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try{
            return extractClaim(token, Claims::getExpiration);
        }catch (ExpiredJwtException e){
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(String username) {
        return createAccessToken(username);
    }
    public String generateRefreshToken(String username) {
        return createRefreshToken(username);
    }


    private String createAccessToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + access_expiration))
                .signWith(getSigningKey()).compact();
    }

    private String createRefreshToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refresh_expiration))
                .signWith(getSigningKey()).compact();
    }

    public Boolean validateToken(String token, String username) {
        final String usernameFromToken = extractUsername(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    public Claims extractAllClaimsIgnoringExpiration(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Use this method to get the username even if the token is expired
    public String extractUsernameIgnoringExpiration(String token) {
        final Claims claims = extractAllClaimsIgnoringExpiration(token);
        return claims.getSubject();
    }
}