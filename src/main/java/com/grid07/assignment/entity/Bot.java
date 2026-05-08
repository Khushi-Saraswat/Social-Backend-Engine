package com.grid07.assignment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "bots")
public class Bot {
    
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String name;


     private String personaDescription;

     public Bot() {

     }

     public Bot(String name, String personaDescription) {
            this.name = name;
            this.personaDescription = personaDescription;
    }



}
