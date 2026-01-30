package com.assecor.assessment.exception;

public class PersonNotFoundException extends RuntimeException {
    
    public PersonNotFoundException(Long id) {
        super("Person not found with ID: " + id);
    }
    
    public PersonNotFoundException(String message) {
        super(message);
    }
}
