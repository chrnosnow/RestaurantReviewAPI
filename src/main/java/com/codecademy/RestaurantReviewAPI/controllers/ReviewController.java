package com.codecademy.RestaurantReviewAPI.controllers;


import com.codecademy.RestaurantReviewAPI.entities.Restaurant;
import com.codecademy.RestaurantReviewAPI.entities.Review;
import com.codecademy.RestaurantReviewAPI.entities.User;
import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
import com.codecademy.RestaurantReviewAPI.repositories.RestaurantRepository;
import com.codecademy.RestaurantReviewAPI.repositories.ReviewRepository;
import com.codecademy.RestaurantReviewAPI.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/reviews")
public class ReviewController {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    public ReviewController(ReviewRepository reviewRepository,
                            UserRepository userRepository,
                            RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return (List<Review>) this.reviewRepository.findAll();
    }

    @GetMapping("/user/{username}")
    public List<Review> getAllUserReviews(@PathVariable("username") String userName) {
        Optional<User> userOptional = this.userRepository.findByNameIgnoreCase(userName);
        if (!userOptional.isPresent() || !this.userRepository.existsUserByNameIgnoreCase(userName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return this.reviewRepository.findAllByUserNameOrderByIdDesc(userName);
    }

    //get all reviews of a restaurant
    @GetMapping("/restaurant/{restaurantid}")
    public List<Review> getAllRestaurantReviews(@PathVariable("restaurantid") Long id) {
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(id);
        if (!restaurantOptional.isPresent() || !this.restaurantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return this.reviewRepository.findAllByRestaurantId(id);
    }


    @PostMapping("/user/{userid}")
    public Review submitReview(@PathVariable("userid") Long id, @RequestBody Review review) {
        if (this.userRepository.findById(id).equals(this.userRepository.findByNameIgnoreCase(review.getUserName()))) {
            Optional<User> userOptional = this.userRepository.findById(id);
            Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(review.getRestaurantId());

            if (!userOptional.isPresent() || !this.userRepository.existsUserByNameIgnoreCase(review.getUserName())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Create a user account first!");
            }

            if (!restaurantOptional.isPresent() || !this.restaurantRepository.existsById(review.getRestaurantId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant doesn't exist!");
            }

            review.setStatus(ReviewStatus.PENDING);
            Review pendingReview = this.reviewRepository.save(review);
            return pendingReview;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username might not match the user id.");
        }
    }

    //update user review
    @PatchMapping("/{userid}/{reviewid}")
    public Review updateReview(@PathVariable("userid") Long userId, @PathVariable("reviewid") Long reviewId,
                               @RequestBody JsonPatch patch) {
        if (this.userRepository.findById(userId).equals(this.userRepository.findByNameIgnoreCase(this.reviewRepository.findById(reviewId).get().getUserName()))) {
            Optional<User> userOptional = this.userRepository.findById(userId);
            Optional<Review> reviewOptional = this.reviewRepository.findById(reviewId);
            if (!userOptional.isPresent() || !this.userRepository.existsById(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            if (!reviewOptional.isPresent() || !this.reviewRepository.existsById(reviewId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            Review reviewToUpdate = reviewOptional.get();
            Review reviewPatched = applyPatchToReview(patch, reviewToUpdate);
            reviewToUpdate.setStatus(ReviewStatus.PENDING);
            return this.reviewRepository.save(reviewPatched);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username might not match the user id.");
        }

    }

    private Review applyPatchToReview(JsonPatch patch, Review targetReview) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = null;
        try {
            patched = patch.apply(objectMapper.convertValue(targetReview, JsonNode.class));
            return objectMapper.treeToValue(patched, Review.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            // TODO: 23/02/17 find a better way to handle this
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public Review deleteReview(@PathVariable("id") Long id) {
        Optional<Review> reviewToDeleteOptional = this.reviewRepository.findById(id);
        if (!reviewToDeleteOptional.isPresent() || !this.reviewRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Review reviewToDelete = reviewToDeleteOptional.get();
        this.reviewRepository.delete(reviewToDelete);
        return reviewToDelete;
    }



}
