package com.assecor.assessment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test to verify the Spring Boot application context loads correctly.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "csv.file.path=sample-input.csv"
})
class AssecorAssessmentApplicationTests {
    
    @Test
    void contextLoads() {
        // This test verifies that the application context loads successfully
    }
}
