package com.inditex.priceservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * End-to-End tests for the Price Service API.
 * This test class starts the full Spring Boot application on a random port
 * and uses RestAssured to perform real HTTP requests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriceE2ETest {

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "/api/prices/applicable";
    private static final long PRODUCT_ID = 35455L;
    private static final long BRAND_ID = 1L;

    @BeforeEach
    public void setup() {
        // Configure RestAssured to use the random port for this test run
        RestAssured.port = port;
    }

    /**
     * Helper method to perform a test request and validate the common parts of the response.
     * @param applicationDate The date string for the request.
     * @param expectedPrice The expected final price.
     * @param expectedPriceList The expected price list ID.
     */
    private void performTest(String applicationDate, float expectedPrice, int expectedPriceList) {
        given()
            .queryParam("applicationDate", applicationDate)
            .queryParam("productId", PRODUCT_ID)
            .queryParam("brandId", BRAND_ID)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("finalPrice", equalTo(expectedPrice))
            .body("priceList", equalTo(expectedPriceList))
            .body("productId", equalTo((int) PRODUCT_ID))
            .body("brandId", equalTo((int) BRAND_ID));
    }

    @Test
    @DisplayName("Test 1: Request at 10:00 on day 14 should return price 35.50")
    void test1_10am_day14() {
        performTest("2020-06-14T10:00:00", 35.50f, 1);
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on day 14 should return price 25.45")
    void test2_4pm_day14() {
        performTest("2020-06-14T16:00:00", 25.45f, 2);
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on day 14 should return price 35.50")
    void test3_9pm_day14() {
        performTest("2020-06-14T21:00:00", 35.50f, 1);
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on day 15 should return price 30.50")
    void test4_10am_day15() {
        performTest("2020-06-15T10:00:00", 30.50f, 3);
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on day 16 should return price 38.95")
    void test5_9pm_day16() {
        performTest("2020-06-16T21:00:00", 38.95f, 4);
    }
}

