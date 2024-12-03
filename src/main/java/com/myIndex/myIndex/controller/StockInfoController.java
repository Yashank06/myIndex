package com.myIndex.myIndex.controller;

import com.myIndex.myIndex.model.StockInfo;
import com.myIndex.myIndex.service.StockInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/myIndex/stocks")
public class StockInfoController {

    @Autowired
    private StockInfoService stockInfoService;

    @PostMapping("/upload")
    public String uploadStockFile(@RequestParam("file") MultipartFile file) {
        try {
            // Process the file
            stockInfoService.saveStocksFromFile(file);
            return "File processed successfully.";
        } catch (IOException e) {
            e.fillInStackTrace();
            return "Failed to process file: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockInfo>> searchStocks( @RequestParam(required = false) String name){
        List<StockInfo> stocks = stockInfoService.searchStocks(name);

        if (stocks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(stocks);
    }
}
