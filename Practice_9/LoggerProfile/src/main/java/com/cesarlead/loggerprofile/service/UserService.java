package com.cesarlead.loggerprofile.service;

import com.cesarlead.loggerprofile.config.AppConstants;
import com.cesarlead.loggerprofile.dto.UserDTO;
import com.cesarlead.loggerprofile.mapper.UserMapper;
import com.cesarlead.loggerprofile.model.User;
import com.cesarlead.loggerprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public List<UserDTO> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO createUser(UserDTO userDTO) {
        log.debug(AppConstants.LOG_ENTERING_METHOD, "createUser");
        log.trace("DTO recibido para creación: {}", userDTO);

        try {
            User user = userMapper.toEntity(userDTO);
            User savedUser = userRepository.save(user);

            log.info(AppConstants.LOG_USER_CREATED, savedUser.getId());

            UserDTO savedUserDTO = userMapper.toDTO(savedUser);

            // Llamada al servicio de email (Mock o Real según el perfil)
            emailService.sendWelcomeEmail(savedUserDTO);

            return savedUserDTO;

        } catch (Exception e) {
            log.error(AppConstants.LOG_ERROR_CREATING_USER, e.getMessage(), e);
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    public void logBien(String transactionId) {
        // --- La Buena Práctica: @Slf4j y Placeholders ---
        // 1. @Slf4j inyecta 'log' automáticamente. Es limpio y sin boilerplate.
        // 2. Usamos placeholders '{}'.
        // 3. Eficiente: La concatenación SÓLO ocurre si el nivel DEBUG está habilitado.
        // 4. Legible: Mucho más fácil de leer.
        log.debug("BUENA PRÁCTICA: Procesando transacción {} para el usuario {}", transactionId, "admin");
    }
}