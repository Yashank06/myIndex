package com.myIndex.myIndex.repository;

import com.myIndex.myIndex.model.MyStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyStockRepository extends JpaRepository<MyStock, Long> {
    Optional<MyStock> findByStockSymbol(String stockSymbol);

    @Override
    List<MyStock> findAll();
}
