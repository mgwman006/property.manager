package tz.tante.rent.manager.configs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tz.tante.rent.manager.models.dtos.ApiResponse;
import tz.tante.rent.manager.utilities.Constant;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint
{
  private final ObjectMapper objectMapper;

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException
  ) throws IOException
  {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    response.getWriter().write(
      objectMapper.writeValueAsString(
        ApiResponse.failure(
          Constant.UNAUTHORIZED_MESSAGE,
          authException.getMessage(),
          HttpStatus.UNAUTHORIZED.value()
        )
      )
    );
  }
}
