package com.myIndex.myIndex.service;

import com.myIndex.myIndex.model.StockInfo;
import com.myIndex.myIndex.repository.StockInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockInfoService {

    @Autowired
    private StockInfoRepository stockInfoRepository;

    public void saveStocksFromFile(MultipartFile file) throws IOException {
        List<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream()))
                .lines()
                .toList();

        for (String line : lines) {
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String name = parts[0].trim();
            String symbol = parts[1].replace("\"", "").trim();
            String industryType = parts[2].replace("\"", "").trim();
            String marketCapString = parts[3].trim().replace("\"", "").replace(",", "");

            // Remove " Cr" if present
            if (marketCapString.endsWith("Cr")) {
                marketCapString = marketCapString.replace("Cr", "").trim();
            }

            double marketCap = 0.0;
            try {
                marketCap = Double.parseDouble(marketCapString); // Parse the cleaned value
            } catch (NumberFormatException e) {
                System.err.println("Invalid market cap format for stock: " + name);
            }

            StockInfo stockInfo = new StockInfo();
            stockInfo.setStockName(name);
            stockInfo.setStockSymbol(symbol);
            stockInfo.setIndustryType(industryType);
            stockInfo.setMarketCap(String.valueOf(marketCap));

            saveOrUpdateStock(stockInfo); // Use the saveOrUpdate method
        }
    }

    public void saveOrUpdateStock(StockInfo stockInfo) {
        Optional<StockInfo> existingStock = stockInfoRepository.findByStockSymbol(stockInfo.getStockSymbol());
        if (existingStock.isPresent()) {
            StockInfo stockToUpdate = existingStock.get();
            stockToUpdate.setStockName(stockInfo.getStockName());
            stockToUpdate.setIndustryType(stockInfo.getIndustryType());
            stockToUpdate.setMarketCap(stockInfo.getMarketCap());
            stockInfoRepository.save(stockToUpdate);
            return;
        }
        stockInfoRepository.save(stockInfo);
    }

    public List<StockInfo> searchStocks(String name) {
        // Search by name if provided
        if (name != null && !name.isEmpty()) {
            return new ArrayList<>(stockInfoRepository.findByStockNameContainingIgnoreCase(name));
        }
        return List.of();
    }


}
