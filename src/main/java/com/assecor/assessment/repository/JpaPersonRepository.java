package com.assecor.assessment.repository;

import com.assecor.assessment.model.Person;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA-based repository for Person entities.
 * This provides database persistence as a bonus feature.
 * Activated when data.source=database property is set.
 */
@Repository
@ConditionalOnProperty(name = "data.source", havingValue = "database")
public interface JpaPersonRepository extends JpaRepository<Person, Long>, PersonRepository {
    
    /**
     * Find persons by color (case-insensitive).
     * 
     * @param color the color name
     * @return list of persons with the specified color
     */
    List<Person> findByColorIgnoreCase(String color);
    
    @Override
    default List<Person> findByColor(String color) {
        return findByColorIgnoreCase(color);
    }
}
