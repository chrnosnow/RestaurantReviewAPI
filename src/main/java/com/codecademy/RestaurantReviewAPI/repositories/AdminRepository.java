package com.codecademy.RestaurantReviewAPI.repositories;

import com.codecademy.RestaurantReviewAPI.entities.AdminReviewAction;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<AdminReviewAction, Long> {
}
