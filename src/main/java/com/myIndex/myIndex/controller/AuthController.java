package com.myIndex.myIndex.controller;

import com.myIndex.myIndex.model.MyStock;
import com.myIndex.myIndex.model.Price;
import com.myIndex.myIndex.service.MyStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/myIndex")
public class AuthController {

    @Autowired
    private MyStockService myStockService;

    @GetMapping("/myAllStocks")
    public ResponseEntity<List<MyStock>> myStocks() {
        List<MyStock> myStockList = myStockService.findAllStock();
        return ResponseEntity.ok(myStockList);
    }

    @PostMapping("/myStock")
    public ResponseEntity<String> registerMyStock(@RequestBody MyStock myStock) {
        myStockService.registerMyStock(myStock);
        return ResponseEntity.ok("Stock registered successfully!");
    }

    @GetMapping("/myStock/{stockSymbol}")
    public ResponseEntity<MyStock> getMyStock(@PathVariable String stockSymbol) {
        MyStock myStock = myStockService.getMyStock(stockSymbol);
        return ResponseEntity.ok(myStock);
    }

    @GetMapping("/stockPrice/{stockSymbol}")
    public ResponseEntity<Price> getStockPrice(@PathVariable String stockSymbol) {
        try {
            Price price = myStockService.fetchStockPrice(stockSymbol);
            return ResponseEntity.ok(price);
        } catch (IOException e) {
            return null;
        }
    }
}
