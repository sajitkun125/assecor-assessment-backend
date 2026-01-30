package com.assecor.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Assecor Assessment Backend.
 * This Spring Boot application provides a RESTful interface for managing
 * persons and their favorite colors.
 */
@SpringBootApplication
public class AssecorAssessmentApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AssecorAssessmentApplication.class, args);
    }
}
