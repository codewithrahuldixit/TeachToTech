package com.rahul.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Transactions {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long count;   
    
    @Column(nullable = false)
    private String transactionId;
}
