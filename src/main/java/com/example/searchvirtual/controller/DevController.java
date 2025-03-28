package com.example.searchvirtual.controller;

import com.example.searchvirtual.domain.Document;
import com.example.searchvirtual.domain.SearchKeyword;
import com.example.searchvirtual.repository.DocumentRepository;
import com.example.searchvirtual.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final DocumentRepository documentRepository;
    private final SearchKeywordRepository  searchKeywordRepository;

    private final String[] dummyKeywords = {
            "java", "java 21", "spring", "virtual thread", "stream", "lambda", "jpa", "hibernate", "rest api", "thread"
    };

    @PostMapping("/init")
    public String generateDummyData(@RequestParam(defaultValue = "100") int count){
        Random random = new Random();

        for (int i =1; i<=count; i++){
            Document doc = new Document();
            doc.setTitle("샘플문서 제목"+i);
            doc.setContent("이 문서는 Java 21과 Virtual Thread를 설명하는 예시입니다. 문서 번호: " + i);
            doc.setCategory("java");
            doc.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            documentRepository.save(doc);
        }

        for (int i =0; i<count; i++){
            SearchKeyword keyword = new SearchKeyword();
            keyword.setKeyword(dummyKeywords[random.nextInt(dummyKeywords.length)]);
            keyword.setSearchedAt(LocalDateTime.now().minusDays(random.nextInt(72)));
            searchKeywordRepository.save(keyword);
        }

        return count + "개의 더미 데이터가 생성되었습니다.";
    }



}
