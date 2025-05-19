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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserProfileIntegrationTest extends IntegrationTestBase {
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
    @DisplayName("Должен вернуть user-profile")
    void shouldReturnUserProfileDto() throws Exception {
        mockMvc.perform(get("/api/v1/user-profile").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("sdfssdfdf"))
                .andExpect(jsonPath("$.avatarUrl").value("sdfsdfsdfsdfsdf"));
    }

    @Test
    @DisplayName("Должен обновить user-profile")
    void shouldUpdateUserProfileDto() throws Exception {
        String json = "{\"name\": \"New Shop\", \"avatarUrl\": \"New Shop\"}";

        mockMvc.perform(put("/api/v1/user-profile")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/user-profile").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Shop"))
                .andExpect(jsonPath("$.avatarUrl").value("New Shop"));

    }

}
