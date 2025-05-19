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
class ProductIntegrationTest extends IntegrationTestBase {

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
    @DisplayName("Должен вернуть все товары")
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(16));
    }

    @Test
    @DisplayName("Должен вернуть пустой список")
    void shouldReturnEmptyListWhenNoProductsOnPage() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .queryParam("page", "1")
                        .queryParam("size", "20")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @DisplayName("Должен вернуть продукты с категорией")
    void shouldReturnProductsWithCategoryId() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("categoryId", "2")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    @DisplayName("Должен вернуть продукты с названием")
    void shouldReturnProductsWithName() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("name", "ор")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    @DisplayName("Должен вернуть товар по id")
    void shouldReturnProductById() throws Exception {
        mockMvc.perform(get("/api/v1/products/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Должен вернуть статус 404")
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/products/999").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен удалить товар по id")
    void shouldReturnNotFoundAfterDeletingProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/1").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен создать товар")
    void shouldCreateProduct() throws Exception {
        String json = "{\"name\": \"New Product\", \"info\": \"New Product\", \"categoryIds\": [1, 2]}";

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    @DisplayName("Должен создать товар")
    void shouldReturnForbiddenWhenAccessDenied() throws Exception {
        var user = userRepository.findByUsername("user").get();
        var userToken = jwtService.generateToken(user);
        String json = "{\"name\": \"New Product\", \"info\": \"New Product\", \"categoryIds\": [1, 2]}";

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Должен обновить товар")
    void shouldUpdateProduct() throws Exception {
        String json = "{\"name\": \"Updated Product\", \"info\": \"Updated Product\", \"categoryIds\": [1, 2]}";

        mockMvc.perform(put("/api/v1/products/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.info").value("Updated Product"));
    }

    @Test
    @DisplayName("Должен вернуть статус 404")
    void shouldReturnNotFoundWhenUpdatingNonExistingProduct() throws Exception {
        String json = "{\"name\": \"Updated Product\", \"info\": \"Updated Product\", \"categoryIds\": [1, 2]}";

        mockMvc.perform(put("/api/v1/products/999")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен вернуть статус 401")
    void shouldReturnUnauthorizedWhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }
}