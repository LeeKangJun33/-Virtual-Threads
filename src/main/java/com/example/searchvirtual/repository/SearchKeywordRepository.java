package com.example.searchvirtual.repository;

import com.example.searchvirtual.domain.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

    @Query(value = """
        SELECT keyword, COUNT(*) AS cnt
        FROM search_keywords
        WHERE searched_at >= :from
        GROUP BY keyword
        ORDER BY cnt DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTopKeywordsSince(LocalDateTime from);

    @Query(value = """
    SELECT keyword
    FROM (
        SELECT keyword, MAX(searched_at) as last_time
        FROM search_keywords
        WHERE keyword LIKE CONCAT(:prefix, '%')
        GROUP BY keyword
    ) as sub
    ORDER BY sub.last_time DESC
    LIMIT 10
    """, nativeQuery = true)
    List<String> findSuggestionsByPrefix(@Param("prefix") String prefix);



}
