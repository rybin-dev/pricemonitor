package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.dto.UserProfileDto;
import com.senla.pricemonitor.entity.User;
import com.senla.pricemonitor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user-profile")
@Tag(name = "User Profile Controller", description = "Управление профилем текущего пользователя")
public class UserProfileController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить профиль пользователя", description = "Возвращает профиль текущего авторизованного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно получен"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    public UserProfileDto getProfile(@AuthenticationPrincipal User user) {
        return userService.getProfile(user.getId());
    }

    @PutMapping
    @Operation(summary = "Обновить профиль пользователя", description = "Обновляет имя и аватар текущего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные профиля"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    public UserProfileDto updateProfile(@AuthenticationPrincipal
                                        User user,
                                        @RequestBody
                                        UserProfileDto userProfileDto) {
        return userService.update(user.getId(), userProfileDto);
    }
}
