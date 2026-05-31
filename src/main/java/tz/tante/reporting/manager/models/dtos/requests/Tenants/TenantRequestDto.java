package tz.tante.reporting.manager.models.dtos.requests.Tenants;

public record TenantRequestDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email
) {
}
