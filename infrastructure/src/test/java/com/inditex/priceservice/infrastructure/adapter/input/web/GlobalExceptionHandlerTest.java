package com.inditex.priceservice.infrastructure.adapter.input.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the GlobalExceptionHandler.
 * We use @WebMvcTest to test the web layer in isolation. A dummy controller
 * is included to trigger the exceptions we want to test.
 */
@WebMvcTest(controllers = GlobalExceptionHandlerTest.DummyController.class)
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestControllerConfiguration.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;


     @TestConfiguration
    static class TestControllerConfiguration {
        @RestController
        static class DummyController {
            @GetMapping("/test-exception")
            public ResponseEntity<String> testEndpoint(@RequestParam Long productId) {
                // This code will not be reached if the exception is thrown during parameter binding
                return ResponseEntity.ok("This should not be returned");
            }
        }
    }


    /**
     * A minimal controller used only to provide an endpoint for testing.
     * This endpoint requires a Long parameter, which allows us to trigger
     * a MethodArgumentTypeMismatchException by passing an invalid value.
     */
    @RestController
    static class DummyController {
        @GetMapping("/test-exception")
        public ResponseEntity<String> testEndpoint(@RequestParam Long productId) {
            // This code will not be reached if the exception is thrown during parameter binding
            return ResponseEntity.ok("This should not be returned");
        }
    }

    @Test
    @DisplayName("Should return 400 Bad Request when a request parameter has a type mismatch")
    void givenInvalidParameterType_whenRequestIsMade_thenHandlerReturnsBadRequest() throws Exception {
        // Arrange
        String invalidProductId = "abc";
        String expectedErrorMessage = String.format("El parametro 'productId' debe ser de tipo 'Long' pero el valor fue: '%s'", invalidProductId);

        // Perform the request
        mockMvc.perform(get("/test-exception")
                        .queryParam("productId", invalidProductId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(expectedErrorMessage)));
    }
}
