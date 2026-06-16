package tz.tante.property.manager.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private  static final Key key = Keys.hmacShaKeyFor(Constant.jwtSecret.getBytes(StandardCharsets.UTF_8));
  private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);


  public static boolean isValidIssuer(String token)
  {
    try
    {
      String EXPECTED_ISSUER = "tz.tante.auth";
      SignedJWT jwt = SignedJWT.parse(token);
      String issuer = jwt.getJWTClaimsSet().getIssuer();
      log.info(EXPECTED_ISSUER+" == "+issuer);
      return EXPECTED_ISSUER.equals(issuer);
    }
    catch (Exception exception)
    {
      throw new AuthException(exception.getMessage());
    }
  }

  public static Claims getClaims(String token) {
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
