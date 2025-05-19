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
class CategoryIntegrationTest extends IntegrationTestBase {

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
    @DisplayName("Должен вернуть все категории")
    void shouldReturnAllCategories() throws Exception {
        mockMvc.perform(get("/api/v1/categories").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(8));
    }

    @Test
    @DisplayName("Должен вернуть пустой список")
    void shouldReturnEmptyListWhenNoCategoriesOnPage() throws Exception {
        mockMvc.perform(get("/api/v1/categories")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @DisplayName("Должен вернуть категорию по id")
    void shouldReturnCategoryById() throws Exception {
        mockMvc.perform(get("/api/v1/categories/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Должен вернуть статус 404")
    void shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/categories/999").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен удалить категорию по id")
    void shouldReturnNotFoundAfterDeletingCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/categories/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен создать категорию")
    void shouldCreateCategory() throws Exception {
        String json = "{\"name\": \"New Category\"}";

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    @DisplayName("Должен вернуть ошибку")
    void shouldReturnForbiddenWhenAccessDenied() throws Exception {
        var user = userRepository.findByUsername("user").get();
        var userToken = jwtService.generateToken(user);
        String json = "{\"name\": \"New Category\"}";

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Должен обновить категорию")
    void shouldUpdateCategory() throws Exception {
        String json = "{\"name\": \"Updated Category\"}";

        mockMvc.perform(put("/api/v1/categories/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category"));
    }

    @Test
    @DisplayName("Должен вернуть статус 404")
    void shouldReturnNotFoundWhenUpdatingNonExistingCategory() throws Exception {
        String json = "{\"name\": \"Updated Category\"}";

        mockMvc.perform(put("/api/v1/categories/999")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен вернуть статус 401")
    void shouldReturnUnauthorizedWhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isUnauthorized());
    }
}