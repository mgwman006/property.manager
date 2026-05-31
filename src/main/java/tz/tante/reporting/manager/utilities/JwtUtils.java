package tz.tante.reporting.manager.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtils {

  private final Key key = Keys.hmacShaKeyFor(Constant.jwtSecret.getBytes(StandardCharsets.UTF_8));

  public String generateToken(String username, Set<String> roles) {
    return Jwts.builder()
      .setSubject(username)
      .claim("roles", roles)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + Constant.jwtExpirationMs))
      .signWith(key)
      .compact();
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String getUserName(String token) {
    return getClaims(token).getSubject();
  }

  @SuppressWarnings("unchecked")
  public Set<String> getRolesFromToken(String token) {
    Claims claims = getClaims(token);
    return new HashSet<>(((java.util.List<String>) claims.get("roles")));
  }
}
