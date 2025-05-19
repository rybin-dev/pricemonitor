package com.senla.pricemonitor.integration.http;

import com.senla.pricemonitor.integration.IntegrationTestBase;
import com.senla.pricemonitor.repository.UserRepository;
import com.senla.pricemonitor.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class ShopIntegrationTest extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private String adminToken;

    @BeforeEach
    void setup() {
        var admin = userRepository.findByUsername("admin").get();
        adminToken = jwtService.generateToken(admin);
    }

    @Test
    @DisplayName("Должен вернуть все магазины")
    void shouldReturnAllShops() throws Exception {
        mockMvc.perform(get("/api/v1/shops").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(5));
    }

    @Test
    @DisplayName("Должен вернуть пустой список")
    void shouldReturnEmptyListWhenNoShopsOnPage() throws Exception {
        mockMvc.perform(get("/api/v1/shops")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @DisplayName("Должен вернуть магазин по id")
    void shouldReturnShopById() throws Exception {
        mockMvc.perform(get("/api/v1/shops/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Должен вернуть статус 404")
    void shouldReturnNotFoundWhenShopDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/shops/999").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен удалить магазин по id")
    void shouldReturnNotFoundAfterDeletingShop() throws Exception {
        mockMvc.perform(delete("/api/v1/shops/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/shops/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен создать магазин")
    void shouldCreateShop() throws Exception {
        String json = "{\"name\": \"New Shop\", \"info\": \"New Shop\"}";

        mockMvc.perform(post("/api/v1/shops")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Shop"));
    }

    @Test
    @DisplayName("Должен вернуть ошибку")
    void shouldReturnForbiddenWhenAccessDenied() throws Exception {
        var user = userRepository.findByUsername("user").get();
        var userToken = jwtService.generateToken(user);
        String json = "{\"name\": \"New Shop\", \"info\": \"New Shop\"}";

        mockMvc.perform(post("/api/v1/shops")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Должен обновить магазин")
    void shouldUpdateShop() throws Exception {
        String json = "{\"name\": \"Updated Shop\", \"info\": \"Updated Shop\"}";

        mockMvc.perform(put("/api/v1/shops/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Shop"));
    }

    @Test
    @DisplayName("Должен вернуть статус 404")
    void shouldReturnNotFoundWhenUpdatingNonExistingShop() throws Exception {
        String json = "{\"name\": \"Updated Shop\", \"info\": \"Updated Shop\"}";

        mockMvc.perform(put("/api/v1/shops/999")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен вернуть статус 401")
    void shouldReturnUnauthorizedWhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/v1/shops"))
                .andExpect(status().isUnauthorized());
    }
}