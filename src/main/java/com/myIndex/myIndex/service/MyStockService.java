package com.myIndex.myIndex.service;

import com.myIndex.myIndex.model.MyStock;
import com.myIndex.myIndex.model.Price;
import com.myIndex.myIndex.repository.MyStockRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
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

    public MyStock registerMyStock(MyStock myStock) {
        return myStockRepository.save(myStock);
    }

    public MyStock getMyStock(String stockSymbol) {
        return myStockRepository.findByStockSymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found with symbol: " +stockSymbol));
    }

    public Price fetchStockPrice(String symbol) throws IOException {
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

        Price price = new Price();
        if (priceElement != null) {
            String priceSTr = priceElement.attr("data-value");
            if (!priceSTr.isEmpty()) {
                price.setCurrPrice(priceSTr);
            }
        }

        System.out.println(price);

        return price;
    }
}