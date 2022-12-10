package learn.capstone.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import learn.capstone.models.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtConverter {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String ISSUER = "capstone-api";
    private final int EXPIRATION_MINUTES = 15;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    public String getTokenFromUser(AppUser user) {

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("appUserId", user.getAppUserId())
                .claim("authorities", String.join(",", user.getAuthorityNames()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public AppUser getUserFromToken(String token) {

        if (token == null) {
            return null;
        }

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            AppUser user = new AppUser();

            user.setUsername(jws.getBody().getSubject());
            user.setAppUserId((jws.getBody().get("appUserId", Integer.class)));

            String authStr = jws.getBody().get("authorities", String.class);
            List<String> authorities = Arrays.stream(authStr.split(","))
                    .filter(a -> a != null && a.trim().length() != 0)
                    .collect(Collectors.toList());
            user.setAuthorityNames(authorities);

            return user;

        } catch (JwtException e) {
            System.out.println(e);
        }

        return null;
    }

}