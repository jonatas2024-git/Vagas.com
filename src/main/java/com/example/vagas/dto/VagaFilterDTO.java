package com.example.vagas.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

// Requer o Lombok
@Data
public class VagaFilterDTO {
    
    private String query;         
    private String location;      

    private BigDecimal minSalary; 
    private String contractType;  
    private String experienceLevel; 
    private LocalDate publishedAfter; 
}