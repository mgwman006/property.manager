package tz.tante.property.manager.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.nimbusds.jwt.SignedJWT;
import tz.tante.property.manager.exceptions.AuthException;


@Component
public class JwtUtils
{

  private final Key key = Keys.hmacShaKeyFor(Constant.jwtSecret.getBytes(StandardCharsets.UTF_8));

  public String generateToken(String username, Set<String> roles) {
    return Jwts.builder()
      .setSubject(username)
      .setIssuer("tz.tante.auth")
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + Constant.jwtExpirationMs))
      .signWith(key)
      .compact();
  }


  public static boolean isValidIssuer(String token)
  {
    try
    {
      String EXPECTED_ISSUER = "https://auth.example.com";
      SignedJWT jwt = SignedJWT.parse(token);
      String issuer = jwt.getJWTClaimsSet().getIssuer();
      return EXPECTED_ISSUER.equals(issuer);
    }
    catch (Exception exception)
    {
      throw new AuthException(exception.getMessage());
    }
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
    Object roles = claims.get("roles");
    if (roles == null) {
      return new HashSet<>();
    }
    return new HashSet<>(((java.util.List<String>) claims.get("roles")));
  }
}
