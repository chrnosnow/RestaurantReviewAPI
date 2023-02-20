package com.codecademy.RestaurantReviewAPI.repositories;

import com.codecademy.RestaurantReviewAPI.entities.Review;

import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findAllByUserNameOrderByIdDesc(String userName);

    List<Review> findAllByRestaurantIdOrderByUserNameAsc(Long restaurantId);

    List<Review> findAllByRestaurantId(Long id);

    @Query("SELECT AVG(peanutScore) FROM Review WHERE restaurantId = :restaurantId AND peanutScore IS NOT NULL AND " +
            "status = APPROVED")
    Double averagePeanutScore(@Param("restaurantId") Long restaurantId);

    @Query("SELECT AVG(eggScore) FROM Review WHERE restaurantId = :restaurantId AND eggScore IS NOT NULL AND status =" +
            " APPROVED")
    Double averageEggScore(@Param("restaurantId") Long restaurantId);

    @Query("SELECT AVG(dairyScore) FROM Review WHERE restaurantId = :restaurantId AND dairyScore IS NOT NULL AND " +
            "status = APPROVED")
    Double averageDairyScore(@Param("restaurantId") Long restaurantId);
}
