package com.example.searchvirtual.service;


import com.example.searchvirtual.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchKeywordService {

    private final SearchKeywordRepository repository;

    public List<Map<String,Object>> getTrending(String range){
        LocalDateTime form;

        switch (range.toLowerCase()){
            case "weekly" -> form = LocalDateTime.now().minusWeeks(1);
            case "monthly" -> form = LocalDateTime.now().minusMonths(1);
            default -> form = LocalDateTime.now().minusDays(1);// 매일
        }

        List<Object[]> result = repository.findTopKeywordsSince(form);

        List<Map<String,Object>> response = new ArrayList<>();
        for (Object[] row : result) {
            Map<String,Object> map = new HashMap<>();
            map.put("keyword", row[0]);
            map.put("count", row[1]);
            response.add(map);
        }

        return response;
    }

    public List<String> getAutocompleteSuggestions(@Param("prefix") String prefix){
        return repository.findSuggestionsByPrefix(prefix);
    }
}
