package com.assecor.assessment.controller;

import com.assecor.assessment.model.Person;
import com.assecor.assessment.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Person management.
 * Provides endpoints for retrieving and creating person data.
 */
@Slf4j
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    
    private final PersonService personService;
    
    /**
     * GET /persons
     * Retrieve all persons.
     * 
     * @return list of all persons with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        log.info("GET /persons - Retrieving all persons");
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }
    
    /**
     * GET /persons/{id}
     * Retrieve a specific person by ID.
     * 
     * @param id the person's ID (CSV line number)
     * @return the person with HTTP 200 OK
     * @throws com.assecor.assessment.exception.PersonNotFoundException if person not found (HTTP 404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        log.info("GET /persons/{} - Retrieving person by ID", id);
        Person person = personService.getPersonById(id);
        return ResponseEntity.ok(person);
    }
    
    /**
     * GET /persons/color/{color}
     * Retrieve all persons with a specific favorite color.
     * 
     * @param color the color name (e.g., "blau", "grün")
     * @return list of persons with the specified color with HTTP 200 OK
     */
    @GetMapping("/color/{color}")
    public ResponseEntity<List<Person>> getPersonsByColor(@PathVariable String color) {
        log.info("GET /persons/color/{} - Retrieving persons by color", color);
        List<Person> persons = personService.getPersonsByColor(color);
        
        if (persons.isEmpty()) {
            log.info("No persons found with color: {}", color);
        }
        
        return ResponseEntity.ok(persons);
    }
    
    /**
     * POST /persons
     * Create a new person (bonus feature).
     * 
     * @param person the person data to create
     * @return the created person with HTTP 201 Created
     * @throws com.assecor.assessment.exception.InvalidRequestException if person data is invalid (HTTP 400)
     */
    @PostMapping
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        log.info("POST /persons - Creating new person: {} {}", 
                person != null ? person.getName() : "null", 
                person != null ? person.getLastname() : "null");
        Person createdPerson = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }
}
