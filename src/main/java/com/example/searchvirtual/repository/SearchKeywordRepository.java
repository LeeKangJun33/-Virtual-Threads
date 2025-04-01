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

    List<SearchKeyword> findTop20ByUsernameOrderBySearchedAtDesc(String username);



    @Query("""
    SELECT k.keyword, COUNT(k) 
    FROM SearchKeyword k 
    WHERE k.username = :username 
    GROUP BY k.keyword 
    ORDER BY COUNT(k) DESC 
    LIMIT 5
""")
    List<Object[]> findTopKeywordsByUsername(@Param("username") String username);

    @Query("""
    SELECT FUNCTION('DATE_FORMAT', k.searchedAt, '%Y-%m-%d'), COUNT(k) 
    FROM SearchKeyword k 
    WHERE k.username = :username 
    GROUP BY FUNCTION('DATE_FORMAT', k.searchedAt, '%Y-%m-%d') 
    ORDER BY FUNCTION('DATE_FORMAT', k.searchedAt, '%Y-%m-%d') DESC
""")
    List<Object[]> findDailyKeywordCountByUsername(@Param("username") String username);

    @Query("""
    SELECT k.keyword, COUNT(k) 
    FROM SearchKeyword k 
    GROUP BY k.keyword 
    ORDER BY COUNT(k) DESC 
    LIMIT 10
""")
    List<Object[]> findGlobalTopKeywords();

    @Query("""
    SELECT FUNCTION('DATE_FORMAT', k.searchedAt, '%Y-%m-%d'), COUNT(k)
    FROM SearchKeyword k 
    GROUP BY FUNCTION('DATE_FORMAT', k.searchedAt, '%Y-%m-%d') 
    ORDER BY FUNCTION('DATE_FORMAT', k.searchedAt, '%Y-%m-%d') DESC
""")
    List<Object[]> findGlobalDailyKeywordStats();



}
