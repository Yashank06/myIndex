package com.myIndex.myIndex.service;

import com.myIndex.myIndex.model.MyStock;
import com.myIndex.myIndex.model.Price;
import com.myIndex.myIndex.repository.MyStockRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MyStockService {

    @Autowired
    private MyStockRepository myStockRepository;

    public List<MyStock> findAllStock(){
        return myStockRepository.findAll();
    }

    public void registerMyStock(MyStock myStock) {
        myStockRepository.save(myStock);
    }

    public MyStock getMyStock(String stockSymbol) {
        return myStockRepository.findByStockSymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found with symbol: " +stockSymbol));
    }

    public Price getPriceFromDatabase(String stockSymbol) throws IOException {
        MyStock stock = myStockRepository.findByStockSymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found with symbol: " + stockSymbol));

        Price price = new Price();
        if(stock.getCurrPrice() == null || stock.getCurrPrice() == "") {
            price.setCurrPrice(fetchStockPrice(stockSymbol));
        } else {
            price.setCurrPrice(stock.getCurrPrice());
        }

        return price;
    }

    public String fetchStockPrice(String symbol) throws IOException {
        String price = null;
        // Append ".NS" for NSE stocks
        String nseSymbol = symbol + ".NS";
        // Construct the URL for the stock on Yahoo Finance
        String url = "https://finance.yahoo.com/quote/" + nseSymbol;
        // Fetch and parse the HTML document
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .timeout(10000)
                .get();
        // Use a precise selector to locate the <fin-streamer> tag with the class `livePrice` and the specific `data-symbol`
        Element priceElement = document.selectFirst("fin-streamer[data-symbol=" + nseSymbol + "][data-field=regularMarketPrice]");
        // Extract the price from the 'data-value' attribute
        System.out.println(priceElement);
        if (priceElement != null) {
            String priceSTr = priceElement.attr("data-value");
            if (!priceSTr.isEmpty()) {
                price = priceSTr;
            }
        }
        System.out.println(price);
        return price;
    }

    // Scheduled task to update stock prices every 10 minutes
    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void updateStockPrices() {
        List<MyStock> stocks = myStockRepository.findAll();
        for (MyStock stock : stocks) {
            try {
                String latestPrice = fetchStockPrice(stock.getStockSymbol());
                // Update the database only if a valid price is retrieved
                if (latestPrice != null) {
                    stock.setCurrPrice(latestPrice);
                    myStockRepository.save(stock); // Save to the database
                    System.out.println("Updated price for " + stock.getStockSymbol() + ": " + latestPrice);
                } else {
                    System.out.println("Skipping update for " + stock.getStockSymbol() + ": Price not available");
                }
            } catch (IOException e) {
                System.err.println("Error fetching price for " + stock.getStockSymbol() + ": " + e.getMessage());
            }
        }
    }
}
