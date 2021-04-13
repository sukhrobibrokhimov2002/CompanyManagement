package uz.pdp.lesson5task1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.lesson5task1.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private long expireDate = 3600_000_000l;
    private String key = "thisIsExtremelySecret";

    public String generateToken(String username,Set<Role> roleSet) {
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireDate))
                .claim("roles", roleSet)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        String username = Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return username;
    }
}
