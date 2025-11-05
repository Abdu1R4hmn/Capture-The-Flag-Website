package com.example.springboot.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByType(String type);



    @Query(value = """
    SELECT c.id AS id, c.type AS type, COUNT(ch.id) AS totalChallenges
    FROM category c
    LEFT JOIN challenge ch ON c.id = ch.category_id
    GROUP BY c.id, c.type
    ORDER BY c.id
    LIMIT :size OFFSET :offset
    """, nativeQuery = true)
    List<CategoryChallengeCountDTO> fetchCategoryChallengeCountsPaged(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM category", nativeQuery = true)
    int countAllCategories();  // This gives you the total number of categories, not challenges.

    @Query(value = """
    SELECT c.id AS id, c.type AS type, COUNT(ch.id) AS totalChallenges
    FROM category c
    LEFT JOIN challenge ch ON c.id = ch.category_id
    WHERE c.type = :type
    GROUP BY c.id, c.type
    """, nativeQuery = true)
    CategoryChallengeCountDTO fetchCategoryChallengeCountByType(@Param("type") String type);


}
