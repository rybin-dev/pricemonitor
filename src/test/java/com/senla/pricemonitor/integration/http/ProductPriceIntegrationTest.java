package com.senla.pricemonitor.integration.http;

import com.senla.pricemonitor.entity.ProductPrice;
import com.senla.pricemonitor.integration.IntegrationTestBase;
import com.senla.pricemonitor.repository.ProductPriceRepository;
import com.senla.pricemonitor.repository.UserRepository;
import com.senla.pricemonitor.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class ProductPriceIntegrationTest extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final ProductPriceRepository productPriceRepository;
    private final JwtService jwtService;
    private String adminToken;


    @BeforeEach
    void setup() {
        var admin = userRepository.findByUsername("admin").get();
        adminToken = jwtService.generateToken(admin);
    }

    @Test
    @DisplayName("Должен вернуть цены в магазинах")
    void shouldReturnAllPricesForProduct() throws Exception {
        mockMvc.perform(get("/api/v1/products/1/shop-prices")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    @DisplayName("Должен изменить цену на продукт")
    void shouldChangePriceForProduct() throws Exception {
        String json = "{\"shopId\": 1, \"newPrice\": 101}";
        mockMvc.perform(post("/api/v1/products/1/change-price")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        ProductPrice pp = productPriceRepository.findCurrentPrice(1L, 1L).orElseThrow();
        assertThat(pp.getPrice()).isEqualTo(101L);
    }

    @Test
    @DisplayName("Должен вернуть статистику по продукту")
    void shouldReturnStats() throws Exception {
        mockMvc.perform(get("/api/v1/products/1/stats")
                        .queryParam("startDate", "2025-05-10")
                        .queryParam("endDate", "2025-05-15")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(6))
                .andExpect(jsonPath("$.items[*].min", Matchers.everyItem(Matchers.is(85))))
                .andExpect(jsonPath("$.items[*].max", Matchers.everyItem(Matchers.is(92))));
    }

}
