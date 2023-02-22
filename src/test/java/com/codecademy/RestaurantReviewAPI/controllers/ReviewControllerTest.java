package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.DiningReviewApiApplication;
import com.codecademy.RestaurantReviewAPI.entities.Review;
import com.codecademy.RestaurantReviewAPI.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DiningReviewApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    public void shouldGetAllReviews() {
        ResponseEntity<Review[]> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port +
                "/api/v1/reviews", Review[].class);
        Review[] allReviews = responseEntity.getBody();
        assertEquals(1, allReviews.length);
    }

    @Test
    public void shouldGetAllUserReviews() {
        ResponseEntity<Review[]> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port +
                "/api/v1/reviews/user/LidiaPoe", Review[].class);
        Review[] allUserReviews = responseEntity.getBody();
        assertEquals(1, allUserReviews.length);
    }

    @Test
    public void shouldGetAllRestaurantReviews() {
        ResponseEntity<Review[]> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port +
                "/api/v1/reviews/restaurant/2", Review[].class);
        Review[] allRestReviews = responseEntity.getBody();
        assertEquals(2, allRestReviews.length);
    }

    @Test
    public void shouldCreateReviewByUser() {
        Review newReview = new Review();
        newReview.setUserName("JonLee");
        newReview.setRestaurantId(Long.valueOf(1));

        ResponseEntity<Review> responseEntity = this.testRestTemplate.postForEntity("http://localhost:" + port +
                "/api/v1/reviews/user/2", newReview, Review.class);
        Review reviewResponse = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(newReview.getUserName(), reviewResponse.getUserName());
        assertEquals(newReview.getRestaurantId(), reviewResponse.getRestaurantId());
    }

//    @Test
//    void updateReview() {
//    }

    @Test
    void shouldDeleteReview() {
        ResponseEntity<Void> resp = testRestTemplate.exchange("http://localhost:" + port +
                "/api/v1/reviews/1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void shouldNotDeleteReviewIfDoesNotExist(){
        ResponseEntity<Void> resp = testRestTemplate.exchange("http://localhost:" + port +
                "/api/v1/reviews/100", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}