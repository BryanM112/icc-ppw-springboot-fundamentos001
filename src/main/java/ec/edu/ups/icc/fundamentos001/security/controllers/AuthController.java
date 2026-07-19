package ec.edu.ups.icc.fundamentos001.security.controllers;

// imports packages y clases....

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.ups.icc.fundamentos001.security.dtos.AuthResponseDto;
import ec.edu.ups.icc.fundamentos001.security.dtos.LoginRequestDto;
import ec.edu.ups.icc.fundamentos001.security.dtos.RefreshTokenRequestDto;
import ec.edu.ups.icc.fundamentos001.security.dtos.RegisterRequestDto;
import ec.edu.ups.icc.fundamentos001.security.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth") // Prefijo para todos los endpoints de autenticación
public class AuthController {

    private final AuthService authService; // Servicio de lógica de autenticación

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login - Endpoint público (configurado en SecurityConfig)
     * POST /auth/login
     * 
     */
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        // @Valid valida anotaciones en LoginRequestDto (email, password requeridos)
        AuthResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response); // 200 OK con JWT
    }

    /**
     * Registro - Endpoint público (configurado en SecurityConfig)
     * POST /auth/register
     */
    @Operation(
        summary = "Registrar usuario",
        description = "Crea un nuevo usuario, asigna ROLE_USER y dev"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado correctamente"
        ),
        @ApiResponse
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        // @Valid valida anotaciones en RegisterRequestDto
        AuthResponseDto response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created con JWT
    }


    @PostMapping("/refresh")
public ResponseEntity<AuthResponseDto> refresh(
        @Valid @RequestBody RefreshTokenRequestDto request
) {
    AuthResponseDto response = authService.refresh(request);

    return ResponseEntity.ok(response);
}


@PostMapping("/logout")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void logout(
        @Valid @RequestBody RefreshTokenRequestDto request
) {
    authService.logout(request);
}
}
