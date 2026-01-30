package com.assecor.assessment.controller;

import com.assecor.assessment.exception.PersonNotFoundException;
import com.assecor.assessment.model.Person;
import com.assecor.assessment.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PersonController REST endpoints.
 */
@WebMvcTest(PersonController.class)
class PersonControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private PersonService personService;
    
    private Person testPerson1;
    private Person testPerson2;
    private Person testPerson3;
    
    @BeforeEach
    void setUp() {
        testPerson1 = Person.builder()
                .id(1L)
                .name("Hans")
                .lastname("Müller")
                .zipcode("67742")
                .city("Lauterecken")
                .color("blau")
                .build();
                
        testPerson2 = Person.builder()
                .id(2L)
                .name("Peter")
                .lastname("Petersen")
                .zipcode("18439")
                .city("Stralsund")
                .color("grün")
                .build();
                
        testPerson3 = Person.builder()
                .id(3L)
                .name("Johnny")
                .lastname("Johnson")
                .zipcode("88888")
                .city("made up")
                .color("violett")
                .build();
    }
    
    @Test
    void testGetAllPersons() throws Exception {
        // Given
        List<Person> persons = Arrays.asList(testPerson1, testPerson2, testPerson3);
        when(personService.getAllPersons()).thenReturn(persons);
        
        // When & Then
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Hans")))
                .andExpect(jsonPath("$[0].lastname", is("Müller")))
                .andExpect(jsonPath("$[0].zipcode", is("67742")))
                .andExpect(jsonPath("$[0].city", is("Lauterecken")))
                .andExpect(jsonPath("$[0].color", is("blau")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Peter")))
                .andExpect(jsonPath("$[2].id", is(3)));
    }
    
    @Test
    void testGetPersonById_Found() throws Exception {
        // Given
        when(personService.getPersonById(1L)).thenReturn(testPerson1);
        
        // When & Then
        mockMvc.perform(get("/persons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Hans")))
                .andExpect(jsonPath("$.lastname", is("Müller")))
                .andExpect(jsonPath("$.zipcode", is("67742")))
                .andExpect(jsonPath("$.city", is("Lauterecken")))
                .andExpect(jsonPath("$.color", is("blau")));
    }
    
    @Test
    void testGetPersonById_NotFound() throws Exception {
        // Given
        when(personService.getPersonById(anyLong())).thenThrow(new PersonNotFoundException(999L));
        
        // When & Then
        mockMvc.perform(get("/persons/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Person not found")));
    }
    
    @Test
    void testGetPersonsByColor() throws Exception {
        // Given
        List<Person> bluePersons = Arrays.asList(testPerson1);
        when(personService.getPersonsByColor("blau")).thenReturn(bluePersons);
        
        // When & Then
        mockMvc.perform(get("/persons/color/blau"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].color", is("blau")));
    }
    
    @Test
    void testGetPersonsByColor_EmptyResult() throws Exception {
        // Given
        when(personService.getPersonsByColor(anyString())).thenReturn(Arrays.asList());
        
        // When & Then
        mockMvc.perform(get("/persons/color/orange"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
    
    @Test
    void testCreatePerson() throws Exception {
        // Given
        Person newPerson = Person.builder()
                .name("New")
                .lastname("Person")
                .zipcode("12345")
                .city("TestCity")
                .color("rot")
                .build();
                
        Person savedPerson = Person.builder()
                .id(10L)
                .name("New")
                .lastname("Person")
                .zipcode("12345")
                .city("TestCity")
                .color("rot")
                .build();
                
        when(personService.createPerson(any(Person.class))).thenReturn(savedPerson);
        
        // When & Then
        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("New")))
                .andExpect(jsonPath("$.lastname", is("Person")))
                .andExpect(jsonPath("$.zipcode", is("12345")))
                .andExpect(jsonPath("$.city", is("TestCity")))
                .andExpect(jsonPath("$.color", is("rot")));
    }
    
    @Test
    void testGetPersonsByColor_MultipleResults() throws Exception {
        // Given
        Person anotherGreenPerson = Person.builder()
                .id(7L)
                .name("Anders")
                .lastname("Andersson")
                .zipcode("32132")
                .city("Schweden")
                .color("grün")
                .build();
                
        List<Person> greenPersons = Arrays.asList(testPerson2, anotherGreenPerson);
        when(personService.getPersonsByColor("grün")).thenReturn(greenPersons);
        
        // When & Then
        mockMvc.perform(get("/persons/color/grün"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].color", is("grün")))
                .andExpect(jsonPath("$[1].color", is("grün")));
    }
    
    @Test
    void testCreatePerson_InvalidData_MissingName() throws Exception {
        // Given
        Person invalidPerson = Person.builder()
                .lastname("TestLastname")
                .zipcode("12345")
                .city("TestCity")
                .color("rot")
                .build();
        
        // When & Then
        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPerson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("Validation failed")));
    }
    
    @Test
    void testGetPersonById_InvalidIdType() throws Exception {
        // When & Then
        mockMvc.perform(get("/persons/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("Invalid value")));
    }
}
