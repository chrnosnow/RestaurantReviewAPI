package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.DiningReviewApiApplication;
import com.codecademy.RestaurantReviewAPI.entities.Review;
import com.codecademy.RestaurantReviewAPI.entities.StatusAdmin;
import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DiningReviewApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminReviewControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldPostAdminReview() {
        StatusAdmin status = new StatusAdmin();
        status.setStatus(ReviewStatus.APPROVED);
        this.testRestTemplate.put("http://localhost:" + port + "/api/v1/admin/1/review/1", status);

        ResponseEntity<Review[]> response = this.testRestTemplate.getForEntity("http://localhost:" + port + "/api/v1" +
                "/reviews", Review[].class);
        Review[] reviewList = response.getBody();
        Review review = reviewList[0];

        assertEquals(ReviewStatus.APPROVED, review.getStatus());
    }

    @Test
    void getPendingRestaurantReviews() {
        ResponseEntity<Review[]> response = this.testRestTemplate.getForEntity("http://localhost:" + port + "/api/v1" +
                "/admin/pending", Review[].class);
        Review[] pendingReview = response.getBody();

        assertEquals(3, pendingReview.length);
    }

    @Test
    void getRestaurantAccepted() {
        ResponseEntity<Review[]> response = this.testRestTemplate.getForEntity("http://localhost:" + port + "/api/v1" +
                "/admin/accepted/restaurant/2", Review[].class);
        Review[] acceptedReviews = response.getBody();
        //modify 2 pending reviews of the same restaurant to accepted reviews
        assertEquals(2, acceptedReviews.length);
    }
}