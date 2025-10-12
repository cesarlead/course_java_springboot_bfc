package org.cesarlead.communication.dto;

public record UserDTO(
        Long id,
        String name,
        String email,
        String role
) {

}
