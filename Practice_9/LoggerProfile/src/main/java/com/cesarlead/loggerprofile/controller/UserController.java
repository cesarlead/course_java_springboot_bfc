package com.cesarlead.loggerprofile.controller;

import com.cesarlead.loggerprofile.config.AppProperties;
import com.cesarlead.loggerprofile.dto.UserDTO;
import com.cesarlead.loggerprofile.service.LegacyLogService;
import com.cesarlead.loggerprofile.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AppProperties appProperties;
    private final LegacyLogService legacyLogService; // Servicio para demo de logs

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("GET /api/v1/users solicitado");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("POST /api/v1/users solicitado");
        log.debug("Body recibido: {}", userDTO); // Aceptable en DEBUG

        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        log.info("GET /api/v1/users/config solicitado");

        // 2. NUNCA devolvemos el bean 'appProperties' directamente.

        // 3. Creamos un objeto de respuesta limpio (un Mapa es lo más fácil)
        Map<String, Object> configData = new HashMap<>();
        configData.put("apiKey", appProperties.getApiKey());
        configData.put("emailMock", appProperties.isEmailMock());

        // 4. Devolvemos el Mapa, que Jackson SÍ sabe serializar.
        return ResponseEntity.ok(configData);
    }

    @GetMapping("/logs-demo")
    public ResponseEntity<String> demoLogs() {
        log.info("--- Iniciando Demo de Logs ---");

        // El Mal Log
        legacyLogService.logLegacy("123-ABC");

        // El Buen Log (con @Slf4j)
        userService.logBien("456-XYZ");

        return ResponseEntity.ok("Demo de logs ejecutada. Revisa la consola.");
    }
}