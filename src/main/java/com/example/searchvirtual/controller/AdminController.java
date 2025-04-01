package com.example.searchvirtual.controller;

import com.example.searchvirtual.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final SearchKeywordRepository searchKeywordRepository;

    @GetMapping("/search-stats")
    public Map<String,Object> getGlobalSearchStats(Authentication authentication){
        boolean isAdmin = false;

        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthoiltes()) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    isAdmin = true;
                    break;
                }
            }
        }

        List<Object[]> topKeywords = searchKeywordRepository.findGlobalTopKeywords();
        List<Map<String,Object>> topList = topKeywords.stream().map(row->{
            Map<String,Object> map = new HashMap<>();
            map.put("keyword",row[0]);
            map.put("count",row[1]);
            return map;
        }).toList();

        List<Object[]> dailyStats = searchKeywordRepository.findGlobalDailyKeywordStats();
        List<Map<String, Object>> dailyList = dailyStats.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("count", row[1]);
            return map;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("topKeywords", topList);
        result.put("dailyStats", dailyList);

        return result;
    }
}
