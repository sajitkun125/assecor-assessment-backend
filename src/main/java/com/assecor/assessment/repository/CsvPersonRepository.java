package com.assecor.assessment.repository;

import com.assecor.assessment.config.ColorMapper;
import com.assecor.assessment.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Slf4j
@Repository
@ConditionalOnProperty(name = "data.source", havingValue = "csv", matchIfMissing = true)
public class CsvPersonRepository implements PersonRepository {
    
    private final ColorMapper colorMapper;
    private final Map<Long, Person> personMap = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);
    
    @Value("${csv.file.path:sample-input.csv}")
    private String csvFilePath;
    
    public CsvPersonRepository(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }
    
    /**
     * Load CSV data on startup.
     */
    @PostConstruct
    public void loadCsvData() {
        try {
            log.info("Loading CSV data from: {}", csvFilePath);
            Reader reader = new FileReader(csvFilePath);
            
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .build();
            
            CSVParser csvParser = csvFormat.parse(reader);
            
            long id = 1;
            for (CSVRecord record : csvParser) {
                try {
                    Person person = parseCsvRecord(record, id);
                    if (person != null) {
                        personMap.put(id, person);
                        id++;
                    }
                } catch (Exception e) {
                    log.warn("Error parsing CSV record at line {}: {}", record.getRecordNumber(), e.getMessage());
                }
            }
            
            idCounter.set(id);
            log.info("Successfully loaded {} persons from CSV", personMap.size());
            
        } catch (IOException e) {
            log.error("Error loading CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load CSV file", e);
        }
    }
    
    /**
     * Parse a CSV record into a Person object.
     * Expected format: Lastname, Firstname, Zipcode City, ColorID
     */
    private Person parseCsvRecord(CSVRecord record, long id) {
        if (record.size() < 4) {
            log.warn("Invalid CSV record at line {}: not enough fields", record.getRecordNumber());
            return null;
        }
        
        try {
            String lastname = record.get(0).trim();
            String firstname = record.get(1).trim();
            String zipcodeCity = record.get(2).trim();
            String colorIdStr = record.get(3).trim();
            
            // Parse zipcode and city
            String[] zipcodeAndCity = zipcodeCity.split("\\s+", 2);
            String zipcode = zipcodeAndCity.length > 0 ? zipcodeAndCity[0] : "";
            String city = zipcodeAndCity.length > 1 ? zipcodeAndCity[1] : "";
            
            // Parse color ID and map to color name
            int colorId = Integer.parseInt(colorIdStr);
            String colorName = colorMapper.getColorName(colorId);
            
            return Person.builder()
                    .id(id)
                    .name(firstname)
                    .lastname(lastname)
                    .zipcode(zipcode)
                    .city(city)
                    .color(colorName)
                    .build();
                    
        } catch (NumberFormatException e) {
            log.warn("Invalid color ID in CSV record at line {}", record.getRecordNumber());
            return null;
        }
    }
    
    @Override
    public List<Person> findAll() {
        return new ArrayList<>(personMap.values());
    }
    
    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(personMap.get(id));
    }
    
    @Override
    public List<Person> findByColor(String color) {
        return personMap.values().stream()
                .filter(person -> person.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
    
    @Override
    public Person save(Person person) {
        if (person.getId() == null) {
            person.setId(idCounter.getAndIncrement());
        }
        personMap.put(person.getId(), person);
        return person;
    }
    
    @Override
    public long count() {
        return personMap.size();
    }
}
