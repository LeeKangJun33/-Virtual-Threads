package com.example.searchvirtual.controller;

import com.example.searchvirtual.domain.SearchKeyword;
import com.example.searchvirtual.repository.SearchKeywordRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final SearchKeywordRepository searchKeywordRepository;

    public UserController(SearchKeywordRepository searchKeywordRepository) {
        this.searchKeywordRepository = searchKeywordRepository;
    }

    public Map<String,Object> getCurrentUser(Authentication auth) {
        Map<String,Object> response = new HashMap<>();
        response.put("username",auth.getName());
        response.put("message","현재 로그인된 사용자 정보입니다.");
        return response;
    }


    @GetMapping("/my-searches")
    public List<Map<String,Object>> mysearch(Authentication auth){
        String username = auth.getName();
        List<SearchKeyword> keywords = searchKeywordRepository.findTop20ByUsernameOrderBySearchedAtDesc(username);

        return keywords.stream().map(k ->{
            Map<String ,Object> map = new HashMap<>();
            map.put("keyword",k.getKeyword());
            map.put("searchedAt",k.getSearchedAt());
            return map;
        }).toList();
    }

    @GetMapping("/my-search-stats")
    public Map<String,Object> getMySearchStats(Authentication auth){
        String username = auth.getName();

        List<Object[]> topKeywords = searchKeywordRepository.findTopKeywordsByUsername(username);
        List<Map<String,Object>> topList = topKeywords.stream().map(row ->{
            Map<String ,Object> map = new HashMap<>();
            map.put("keyword",row[0]);
            map.put("count",row[1]);
            return map;
        }).toList();

        List<Object[]> dailyStats  = searchKeywordRepository.findDailyKeywordCountByUsername(username);
        List<Map<String,Object>> dailyList   = dailyStats.stream().map(row ->{
            Map<String,Object> map = new HashMap<>();
            map.put("keyword",row[0]);
            map.put("count",row[1]);
            return map;
        }).toList();

        Map<String ,Object> result = new HashMap<>();
        result.put("topKeywords",topList);
        result.put("dailyStats",dailyList );

        return result;
    }
}
