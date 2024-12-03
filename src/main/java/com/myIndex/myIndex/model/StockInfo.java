package com.myIndex.myIndex.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stockName;

    @Column(unique = true, nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private String industryType;

    @Column(nullable = false)
    private String marketCap;
}
