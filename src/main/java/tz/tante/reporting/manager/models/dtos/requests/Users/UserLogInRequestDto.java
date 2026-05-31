package tz.tante.reporting.manager.models.dtos.requests.Users;

public record UserLogInRequestDto(
        String email,
        String passWord) {
}
