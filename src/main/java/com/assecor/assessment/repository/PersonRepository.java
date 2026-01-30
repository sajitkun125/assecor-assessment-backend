package com.assecor.assessment.repository;

import com.assecor.assessment.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Person data access.
 * This interface abstracts the data source implementation,
 * allowing easy replacement of CSV with database or other sources.
 */
public interface PersonRepository {
    
    /**
     * Find all persons.
     * 
     * @return list of all persons
     */
    List<Person> findAll();
    
    /**
     * Find a person by their ID.
     * 
     * @param id the person's ID
     * @return Optional containing the person if found
     */
    Optional<Person> findById(Long id);
    
    /**
     * Find all persons with a specific favorite color.
     * 
     * @param color the color name
     * @return list of persons with the specified color
     */
    List<Person> findByColor(String color);
    
    /**
     * Save a person (create or update).
     * 
     * @param person the person to save
     * @return the saved person with ID assigned
     */
    Person save(Person person);
    
    /**
     * Get the total count of persons.
     * 
     * @return the count
     */
    long count();
}
