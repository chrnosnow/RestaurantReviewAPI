package com.codecademy.RestaurantReviewAPI.repositories;

import com.codecademy.RestaurantReviewAPI.entities.Restaurant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.beans.Transient;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Iterable<Restaurant> findAllByOrderByNameAsc();

    Optional<Restaurant> findByNameAndCodeIgnoreCase(String name, String zipcode);

    Iterable<Restaurant> findByCodeOrderByCodeDesc(String zipcode);

    List<Restaurant> findByCodeAndPeanutAvgScoreIsNotNullOrderByPeanutAvgScoreDesc(String zipcode);

    List<Restaurant> findByCodeAndEggAvgScoreIsNotNullOrderByEggAvgScoreDesc(String zipcode);

    List<Restaurant> findByCodeAndDairyAvgScoreIsNotNullOrderByDairyAvgScoreDesc(String zipcode);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE RESTAURANTS SET PEANUT_AVG_SCORE = :avgPeanutScore, EGG_AVG_SCORE = :avgEggScore, DAIRY_AVG_SCORE" +
            " = :avgDairyScore, AVG_SCORE = :avgScore WHERE ID = :restaurantId", nativeQuery = true)
    void setAverages(@Param("restaurantId") Long id, @Param("avgPeanutScore") Double averagePeanutScore,
                     @Param("avgEggScore") Double averageEggScore, @Param("avgDairyScore") Double averageDairyScore,
                     @Param("avgScore") Double avgScore);
}
