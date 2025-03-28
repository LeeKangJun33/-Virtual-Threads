package com.example.searchvirtual.service;


import com.example.searchvirtual.domain.Document;
import com.example.searchvirtual.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public List<Document> search(String keyword,int page,int size,String sort,String category) {
        int offset = (page - 1) * size;
        return documentRepository.searchByKeywordWithFilter(keyword, category, sort, size, offset);
    }

    public long count(String keyword,String category) {
        return documentRepository.countByKeywordWithFilter(keyword, category);
    }
}
