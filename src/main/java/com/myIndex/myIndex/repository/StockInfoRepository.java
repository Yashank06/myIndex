package com.myIndex.myIndex.repository;

import com.myIndex.myIndex.model.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
    Optional<StockInfo> findByStockSymbol(String stockSymbol);
    List<StockInfo> findByStockNameContainingIgnoreCase(String name);
}
