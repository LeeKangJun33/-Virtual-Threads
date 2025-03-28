package com.example.searchvirtual.repository;

import com.example.searchvirtual.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query(value = """
    SELECT * FROM documents
    WHERE MATCH(title, content) AGAINST (:keyword IN NATURAL LANGUAGE MODE)
    AND (:category IS NULL OR category = :category)
    ORDER BY 
        CASE WHEN :sort = 'latest' THEN created_at END DESC
    LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Document> searchByKeywordWithFilter(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("sort") String sort,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    // 전체 개수 (Fulltext 검색 결과용)
    @Query(value = """
    SELECT COUNT(*) FROM documents
    WHERE MATCH(title, content) AGAINST (:keyword IN NATURAL LANGUAGE MODE)
    AND (:category IS NULL OR category = :category)
    """, nativeQuery = true)
    long countByKeywordWithFilter(
            @Param("keyword") String keyword,
            @Param("category") String category
    );
}
