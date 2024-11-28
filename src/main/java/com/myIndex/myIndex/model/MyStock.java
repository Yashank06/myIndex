package com.myIndex.myIndex.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MyStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String stockName;

    @Column(name = "stockSymbol",nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private String industryType;

    @Column(nullable = false)
    private String mktCap;

    @Column(nullable = false)
    private String sellRange;

    @Column(nullable = false)
    private String buyRange;

    @Column(nullable = false)
    private String yearlyRating;

    @Column(nullable = false)
    private String qtrRating;

    private String role = "USER"; // Default role
}
