# HW8 - Seekly：使用 Tavily Search API 的搜尋引擎

Spring Boot + Thymeleaf 網頁，輸入關鍵字後呼叫 [Tavily Search API](https://docs.tavily.com/documentation/api-reference/endpoint/search)，把結果（標題、網址、摘要）顯示在自己的頁面上。查無結果時會顯示「沒有找到結果」提示。

## 結構
```
src/main/java/com/example/search/
├── SearchApplication.java            # 進入點
├── controller/SearchController.java  # /、/search、/api/search
└── service/TavilySearchService.java  # 呼叫 Tavily API
src/main/resources/
├── application.properties            # 金鑰由環境變數讀取，不含真實金鑰
├── templates/index.html, result.html
└── static/style.css
```

## 執行
1. 到 https://app.tavily.com 取得 API 金鑰（`tvly-...`）。
2. 提供金鑰（二擇一，**都不會被 commit**）：
   - 環境變數：`export TAVILY_API_KEY=tvly-你的金鑰`
   - 或複製 `src/main/resources/application-local.properties.example` 為 `application-local.properties` 並填入金鑰（已列入 `.gitignore`）
3. 啟動：`mvn spring-boot:run`
4. 開啟 http://localhost:8080
