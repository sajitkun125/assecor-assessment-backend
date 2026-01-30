package com.assecor.assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;
    
    @NotBlank(message = "Name is required")
    @JsonProperty("name")
    @Column(name = "name")
    private String name;
    
    @NotBlank(message = "Lastname is required")
    @JsonProperty("lastname")
    @Column(name = "lastname")
    private String lastname;
    
    @JsonProperty("zipcode")
    @Column(name = "zipcode")
    private String zipcode;
    
    @JsonProperty("city")
    @Column(name = "city")
    private String city;
    
    @NotBlank(message = "Color is required")
    @JsonProperty("color")
    @Column(name = "color")
    private String color;
}
