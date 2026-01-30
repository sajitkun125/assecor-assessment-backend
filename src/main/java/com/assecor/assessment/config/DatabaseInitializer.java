package com.assecor.assessment.config;

import com.assecor.assessment.model.Person;
import com.assecor.assessment.repository.JpaPersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.Reader;

/**
 * Database initializer for loading CSV data into the database.
 * Only activated when using the database data source.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "data.source", havingValue = "database")
public class DatabaseInitializer implements CommandLineRunner {
    
    private final JpaPersonRepository repository;
    private final ColorMapper colorMapper;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing database with CSV data...");
        
        try {
            Reader reader = new FileReader("sample-input.csv");
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .build();
            
            CSVParser csvParser = csvFormat.parse(reader);
            
            int count = 0;
            for (CSVRecord record : csvParser) {
                try {
                    if (record.size() >= 4) {
                        Person person = parseCsvRecord(record);
                        if (person != null) {
                            ((org.springframework.data.jpa.repository.JpaRepository<Person, Long>) repository).save(person);
                            count++;
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error parsing CSV record: {}", e.getMessage());
                }
            }
            
            log.info("Database initialized with {} persons", count);
            
        } catch (Exception e) {
            log.error("Error initializing database: {}", e.getMessage(), e);
        }
    }
    
    private Person parseCsvRecord(CSVRecord record) {
        try {
            String lastname = record.get(0).trim();
            String firstname = record.get(1).trim();
            String zipcodeCity = record.get(2).trim();
            String colorIdStr = record.get(3).trim();
            
            String[] zipcodeAndCity = zipcodeCity.split("\\s+", 2);
            String zipcode = zipcodeAndCity.length > 0 ? zipcodeAndCity[0] : "";
            String city = zipcodeAndCity.length > 1 ? zipcodeAndCity[1] : "";
            
            int colorId = Integer.parseInt(colorIdStr);
            String colorName = colorMapper.getColorName(colorId);
            
            return Person.builder()
                    .name(firstname)
                    .lastname(lastname)
                    .zipcode(zipcode)
                    .city(city)
                    .color(colorName)
                    .build();
                    
        } catch (Exception e) {
            log.warn("Error parsing CSV record: {}", e.getMessage());
            return null;
        }
    }
}
