package tz.tante.reporting.manager.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import tz.tante.reporting.manager.models.dtos.ApiResponse;
import tz.tante.reporting.manager.models.dtos.requests.Users.UserCreateRequestDto;
import tz.tante.reporting.manager.models.dtos.responses.users.UserDetailsDTO;
import tz.tante.reporting.manager.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDetailsDTO>> registerUser(@RequestBody UserCreateRequestDto request)
    {
        UserDetailsDTO createdUser = userService.createUser(request);
        URI location = URI.create("users/"+createdUser.id());
        return ResponseEntity.created(location)
          .body(ApiResponse.success(createdUser,HttpStatus.CREATED.value()));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> getUserById(
      @NotBlank @PathVariable String phoneNumber)
    {
        UserDetailsDTO userDetail  = userService.getUserByPhoneNumber(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK)
          .body(ApiResponse.success(userDetail,HttpStatus.OK.value()));
    }

}
