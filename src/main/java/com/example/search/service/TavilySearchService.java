package com.example.search.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TavilySearchService {

    /** 單筆搜尋結果：標題、網址、摘要 */
    public record SearchResult(String title, String url, String content) {}

    @Value("${tavily.enabled:false}")
    private boolean enabled;

    @Value("${tavily.apiKey:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isConfigured() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }

    @SuppressWarnings("unchecked")
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        if (!isConfigured() || query == null || query.isBlank()) {
            return results;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("query", query);
        body.put("max_results", 10);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, Object>> resp = restTemplate.postForEntity(
                "https://api.tavily.com/search", request,
                (Class<Map<String, Object>>) (Class<?>) Map.class);

        Map<String, Object> respBody = resp.getBody();
        if (respBody == null) {
            return results;
        }

        Object itemsObj = respBody.get("results");
        if (itemsObj instanceof List<?> items) {
            for (Object itemObj : items) {
                if (itemObj instanceof Map<?, ?> item) {
                    String title = str(item.get("title"));
                    String url = str(item.get("url"));
                    String content = str(item.get("content"));
                    if (!title.isEmpty() && !url.isEmpty()) {
                        results.add(new SearchResult(title, url, content));
                    }
                }
            }
        }
        return results;
    }

    private static String str(Object o) {
        return o == null ? "" : o.toString();
    }
}
