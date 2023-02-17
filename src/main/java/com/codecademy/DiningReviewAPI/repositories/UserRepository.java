package com.codecademy.DiningReviewAPI.repositories;

import com.codecademy.DiningReviewAPI.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByNameIgnoreCase(String userName);
    Boolean existsUserByNameIgnoreCase(String userName);
}
