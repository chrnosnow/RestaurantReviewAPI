package com.codecademy.RestaurantReviewAPI.repositories;

import com.codecademy.RestaurantReviewAPI.entities.Restaurant;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Iterable<Restaurant> findAllByOrderByNameAsc();
    Optional<Restaurant> findByNameAndCodeIgnoreCase(String name, String zipcode);
    Iterable<Restaurant> findByCodeOrderByCodeDesc(String zipcode);
    List<Restaurant> findByCodeAndPeanutAvgScoreIsNotNullOrderByPeanutAvgScoreDesc(String zipcode);
    List<Restaurant> findByCodeAndEggAvgScoreIsNotNullOrderByEggAvgScoreDesc(String zipcode);
    List<Restaurant> findByCodeAndDairyAvgScoreIsNotNullOrderByDairyAvgScoreDesc(String zipcode);
}
