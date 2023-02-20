package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.entities.AdminReviewAction;
import com.codecademy.RestaurantReviewAPI.entities.Restaurant;
import com.codecademy.RestaurantReviewAPI.entities.Review;
import com.codecademy.RestaurantReviewAPI.entities.StatusAdmin;
import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
import com.codecademy.RestaurantReviewAPI.repositories.AdminRepository;
import com.codecademy.RestaurantReviewAPI.repositories.RestaurantRepository;
import com.codecademy.RestaurantReviewAPI.repositories.ReviewRepository;
import com.codecademy.RestaurantReviewAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin")
public class AdminReviewController {
    private ReviewRepository reviewRepository;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;
    private AdminRepository adminRepository;

    public AdminReviewController(ReviewRepository reviewRepository,
                                 RestaurantRepository restaurantRepository,
                                 UserRepository userRepository, AdminRepository adminRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @PutMapping("{adminid}/review/{id}")
    public Review adminReview(@PathVariable("id") Long adminId, @PathVariable("id") Long reviewId,
                              @RequestBody StatusAdmin status) {
        Optional<AdminReviewAction> adminOptional = this.adminRepository.findById(adminId);
        Optional<Review> reviewOptional = this.reviewRepository.findById(reviewId);

        if (!adminOptional.isPresent() || !reviewOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Review review = reviewOptional.get();
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(review.getRestaurantId());

        try {
            review.setStatus(status.getStatus());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please approve or reject the review.");
        }

        Review reviewed = this.reviewRepository.save(review);
        if (reviewed.getStatus() == ReviewStatus.APPROVED) {
            updateRestaurantScores(restaurantOptional.get());
        }
        return reviewed;
    }

    @GetMapping("/pending")
    public List<Review> getPendingRestaurantReviews() {
        try {
            List<Review> allReviewsList = (List<Review>) this.reviewRepository.findAll();
            List<Review> pendingReviews = allReviewsList.stream()
                    .filter(review -> review.getStatus() == ReviewStatus.PENDING)
                    .toList();
            return pendingReviews;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to get list of pending reviews.");
        }
    }

    @GetMapping("accepted/restaurant/{id}")
    public List<Review> getRestaurantAccepted(@PathVariable("id") Long restaurantId) {
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(restaurantId);
        if (!restaurantOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }

        try {
            List<Review> reviewsList = this.reviewRepository.findAllByRestaurantId(restaurantId);
            List<Review> approvedReviews = reviewsList.stream()
                    .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                    .toList();
            return approvedReviews;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to get list of pending reviews.");
        }
    }

    private void updateRestaurantScores(Restaurant restaurant) {
        Long restaurantId = restaurant.getId();
        Double averagePeanutScore = this.reviewRepository.averagePeanutScore(restaurantId);
        Double averageEggScore = this.reviewRepository.averageEggScore(restaurantId);
        Double averageDairyScore = this.reviewRepository.averageDairyScore(restaurantId);
        Double averageTotalScore = (averagePeanutScore + averageEggScore + averageDairyScore) / 3;
        this.restaurantRepository.setAverages(restaurantId, averagePeanutScore, averageEggScore, averageDairyScore,
                averageTotalScore);
    }
}
