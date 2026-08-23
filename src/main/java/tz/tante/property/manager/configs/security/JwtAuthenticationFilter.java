package tz.tante.property.manager.configs.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tz.tante.property.manager.exceptions.AuthException;
import tz.tante.property.manager.utilities.JwtUtils;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
  private final JwtAuthenticationEntryPoint authenticationEntryPoint;

  public JwtAuthenticationFilter(JwtAuthenticationEntryPoint authenticationEntryPoint)
  {
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request)
  {
    String path = request.getServletPath();

    return path.startsWith("/swagger-ui")
      || path.startsWith("/v3/api-docs")
      || path.equals("/swagger-ui.html");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws IOException
  {

    try
    {

      String authHeader = request.getHeader("Authorization");

      if (authHeader == null || !authHeader.startsWith("Bearer "))
      {
        authenticationEntryPoint.commence(
          request,
          response,
          new BadCredentialsException("Missing Authorization token")
        );
        return;
      }

      String token = authHeader.substring(7);

      Claims claims = JwtUtils.getClaims(token);

      // ✔ issuer validation
      if (!JwtUtils.isValidIssuer(token)) {
        throw new AuthException("Invalid issuer");
      }

      // ✔ create authentication
      UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(
          claims.getSubject(),
          null,
          List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

      auth.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
      );

      SecurityContextHolder.getContext().setAuthentication(auth);

      filterChain.doFilter(request, response);

    } catch (Exception ex) {
      authenticationEntryPoint.commence(
        request,
        response,
        new BadCredentialsException(ex.getMessage(), ex)
      );
    }
  }
}
