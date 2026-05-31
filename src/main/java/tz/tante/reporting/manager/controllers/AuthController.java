package tz.tante.reporting.manager.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tz.tante.reporting.manager.models.dtos.ApiResponse;
import tz.tante.reporting.manager.models.dtos.requests.account.AccountAuthRequestDTO;
import tz.tante.reporting.manager.models.dtos.requests.account.AccountCreateDto;
import tz.tante.reporting.manager.models.dtos.responses.AccountDetailsDto;
import tz.tante.reporting.manager.models.dtos.responses.AccountAuthResponseDTO;
import tz.tante.reporting.manager.services.AuthService;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController
{
  private final AuthService authService;

  @GetMapping("/account")
  public ResponseEntity<ApiResponse<AccountDetailsDto>> getAccountByPhoneNumber(
    @RequestParam
    @NotBlank(message = "Phone number is required")
    @Pattern(
      regexp = "^(\\+255|255|0)([678])\\d{8}$",
      message = "Invalid Tanzanian phone number"
    )
    String phoneNumber
  )
  {
    return ResponseEntity.ok(
      ApiResponse.success(
        authService.findAccountByPhoneNumber(phoneNumber),
        HttpStatus.OK.value()
      )
    );
  }

  @PostMapping("/account")
  public ResponseEntity<ApiResponse<AccountDetailsDto>> createAccount(
    @Valid @RequestBody AccountCreateDto accountCreateDto)
  {
    AccountDetailsDto accountDetailsDto = authService.createAccount(accountCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(accountDetailsDto, HttpStatus.CREATED.value()));
  }

  @PostMapping()
  public ResponseEntity<ApiResponse<AccountAuthResponseDTO>> authenticate(
    @Valid @RequestBody AccountAuthRequestDTO request)
  {
    AccountAuthResponseDTO response = authService.authenticate(request);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }
}
