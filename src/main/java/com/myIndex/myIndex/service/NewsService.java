package com.myIndex.myIndex.service;

import com.myIndex.myIndex.model.StockNews;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {

    @Value("${newsapi.url}")
    private String apiUrl;

    @Value("${newsapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<StockNews> fetchLatestNews(String stockName) {
        List<StockNews> newsList = new ArrayList<>();

        String url = apiUrl + "?q=" + stockName + "&apiKey=" + apiKey;

        try {
            // Call the news API
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Parse the response
            if (response != null && response.containsKey("articles")) {
                List<Map<String, Object>> articles = (List<Map<String, Object>>) response.get("articles");

                for (Map<String, Object> article : articles) {
                    StockNews stockNews = new StockNews();

                    stockNews.setTitle((String) article.get("title"));
                    stockNews.setUrl((String) article.get("url"));
                    stockNews.setSource(((Map<String, String>) article.get("source")).get("name"));
                    stockNews.setPublishedTime((String) article.get("publishedAt"));

                    newsList.add(stockNews);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}

