package com.example.search.controller;

import com.example.search.service.TavilySearchService;
import com.example.search.service.TavilySearchService.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

@Controller
public class SearchController {

    private final TavilySearchService searchService;

    public SearchController(TavilySearchService searchService) {
        this.searchService = searchService;
    }

    /** 首頁：搜尋框 */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /** 搜尋結果頁 */
    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String q, Model model) {
        if (q == null || q.isBlank()) {
            return "redirect:/";
        }
        String query = q.trim();
        model.addAttribute("query", query);

        List<SearchResult> results = Collections.emptyList();
        String error = null;
        if (!searchService.isConfigured()) {
            error = "尚未設定 Tavily API 金鑰（請設定環境變數 TAVILY_API_KEY）。";
        } else {
            try {
                results = searchService.search(query);
            } catch (RestClientException e) {
                error = "搜尋服務暫時無法使用，請稍後再試。";
            }
        }
        model.addAttribute("results", results);
        model.addAttribute("error", error);
        return "result";
    }

    /** JSON API：/api/search?q=... */
    @GetMapping("/api/search")
    @ResponseBody
    public List<SearchResult> apiSearch(@RequestParam("q") String q) {
        return searchService.search(q.trim());
    }
}
