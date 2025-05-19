package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.dto.AuthenticationRequest;
import com.senla.pricemonitor.dto.RegisterRequest;
import com.senla.pricemonitor.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Управление регистрацией и аутентификацией пользователей")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    @Operation(summary = "Регистрация пользователя", description = "Создаёт нового пользователя с указанными email и паролем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные регистрации")
    })
    public ResponseEntity<?> signup(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    @Operation(summary = "Аутентификация пользователя", description = "Выполняет вход пользователя и возвращает JWT токен.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аутентификация успешна, возвращён токен"),
            @ApiResponse(responseCode = "422", description = "Ошибка аутентификации, неверные данные")
    })
    public ResponseEntity<?> signin(
            @RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.signin(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getLocalizedMessage());
        }
    }
}