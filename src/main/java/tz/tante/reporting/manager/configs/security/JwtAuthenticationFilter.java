package tz.tante.reporting.manager.configs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tz.tante.reporting.manager.utilities.JwtUtils;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
  private final JwtUtils jwtUtils;
  private final JwtAuthenticationEntryPoint authenticationEntryPoint;

  public JwtAuthenticationFilter(JwtUtils jwtUtils, JwtAuthenticationEntryPoint authenticationEntryPoint)
  {
    this.jwtUtils = jwtUtils;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }


  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws IOException
  {
    try
    {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer "))
      {
        String token = authHeader.substring(7);
        if (jwtUtils.validateToken(token))
        {
          String phoneNumber = jwtUtils.getUserName(token);
          List<SimpleGrantedAuthority> roles = jwtUtils.getRolesFromToken(token)
            .stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

          var authToken = new UsernamePasswordAuthenticationToken(phoneNumber, null, roles);
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      filterChain.doFilter(request, response);
    }
    catch (Exception exception)
    {
      authenticationEntryPoint.commence(
        request,
        response,
        new BadCredentialsException(exception.getMessage(),exception)
      );
    }

  }
}
