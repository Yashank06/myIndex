package com.myIndex.myIndex.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MyStock {

    @Id
    @Column(name = "stockSymbol", nullable = false, unique = true)
    private String stockSymbol;

    @Column(unique = true, nullable = false)
    private String stockName;

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

    private String currPrice;

    private String role = "USER"; // Default role
}
