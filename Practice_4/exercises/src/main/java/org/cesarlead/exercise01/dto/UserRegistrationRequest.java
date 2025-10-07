package org.cesarlead.exercise01.dto;

public record UserRegistrationRequest(
        String username,
        String email,
        String password,
        Integer age
) {
}
