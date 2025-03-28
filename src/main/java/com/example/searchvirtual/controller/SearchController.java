package com.example.searchvirtual.controller;

import com.example.searchvirtual.domain.Document;
import com.example.searchvirtual.domain.SearchKeyword;
import com.example.searchvirtual.repository.SearchKeywordRepository;
import com.example.searchvirtual.service.DocumentService;
import com.example.searchvirtual.service.SearchKeywordService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final DocumentService documentService;
    private final SearchKeywordService searchKeywordService;
    private final SearchKeywordRepository searchKeywordRepository;

    @GetMapping("/search")
    public Map<String, Object> searchDocuments(
            @RequestParam("q") String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "relevance") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        //검색 키워드 기록
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword(keyword);
        searchKeyword.setSearchedAt(LocalDateTime.now());
        searchKeywordRepository.save(searchKeyword);


        //검색 결과
        List<Document> result = documentService.search(keyword, page, size,category,sort);
        long total = documentService.count(keyword,category);

        Map<String ,Object> response  = new HashMap<>();
        response.put("total", total);
        response.put("result", result);
        response.put("page", page);
        response.put("size", size);

        return response;
    }

    @GetMapping("/trending")
    public Map<String,Object> trendingKeywords(
            @RequestParam(defaultValue = "daily") String range
    ){
        List<Map<String,Object>> keywords = searchKeywordService.getTrending(range);

        Map<String,Object> response = new HashMap<>();
        response.put("keywords", keywords);
        response.put("range", range);
        return response;
    }

    @GetMapping("/autocomplete")
    public Map<String ,Object> autocomplte(@RequestParam("prefix") String prefix){
        List<String> suggestions = searchKeywordService.getAutocompleteSuggestions(prefix);

        Map<String,Object> response = new HashMap<>();
        response.put("suggestions", suggestions);
        response.put("prefix", prefix);

        return response;
    }


}
