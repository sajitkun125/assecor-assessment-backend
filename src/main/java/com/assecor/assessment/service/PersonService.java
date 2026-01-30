package com.assecor.assessment.service;

import com.assecor.assessment.exception.InvalidRequestException;
import com.assecor.assessment.exception.PersonNotFoundException;
import com.assecor.assessment.model.Person;
import com.assecor.assessment.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Person-related business logic.
 * Uses dependency injection to access the PersonRepository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {
    
    private final PersonRepository personRepository;
    
    /**
     * Get all persons.
     * 
     * @return list of all persons
     */
    public List<Person> getAllPersons() {
        log.debug("Fetching all persons");
        return personRepository.findAll();
    }
    
    /**
     * Get a person by ID.
     * 
     * @param id the person's ID
     * @return the person
     * @throws PersonNotFoundException if person not found
     */
    public Person getPersonById(Long id) {
        log.debug("Fetching person with ID: {}", id);
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
    
    /**
     * Get all persons with a specific favorite color.
     * 
     * @param color the color name
     * @return list of persons with the specified color
     */
    public List<Person> getPersonsByColor(String color) {
        log.debug("Fetching persons with color: {}", color);
        return personRepository.findByColor(color);
    }
    
    /**
     * Create a new person.
     * 
     * @param person the person to create
     * @return the created person with ID assigned
     * @throws InvalidRequestException if person data is invalid
     */
    public Person createPerson(Person person) {
        log.info("Creating new person: {} {}", person.getName(), person.getLastname());
        
        // Validate person data
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Person name cannot be empty");
        }
        if (person.getLastname() == null || person.getLastname().trim().isEmpty()) {
            throw new InvalidRequestException("Person lastname cannot be empty");
        }
        if (person.getColor() == null || person.getColor().trim().isEmpty()) {
            throw new InvalidRequestException("Person color cannot be empty");
        }
        
        person.setId(null); // Ensure new ID is generated
        return personRepository.save(person);
    }
    
    /**
     * Get the total count of persons.
     * 
     * @return the count
     */
    public long getPersonCount() {
        return personRepository.count();
    }
}
