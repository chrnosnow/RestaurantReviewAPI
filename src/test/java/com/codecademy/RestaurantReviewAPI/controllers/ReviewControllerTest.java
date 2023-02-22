package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.DiningReviewApiApplication;
import com.codecademy.RestaurantReviewAPI.entities.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    public void shouldUpdateReview() {
        String url = "http://localhost:" + port + "/api/v1/reviews/1/1";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON);

        String patchString = "[{\n" +
                "    \"op\":\"replace\",\n" +
                "    \"path\": \"/peanutScore\",\n" +
                "    \"value\":1\n" +
                "}]";

        HttpEntity<String> requestEntity = new HttpEntity<String>(patchString, reqHeaders);
        ResponseEntity<Review> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity,
                Review.class);
        Review result = responseEntity.getBody();

        assertEquals(1, result.getPeanutScore());
    }

    @Test
    void shouldDeleteReview() {
        ResponseEntity<Void> resp = testRestTemplate.exchange("http://localhost:" + port +
                "/api/v1/reviews/1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void shouldNotDeleteReviewIfDoesNotExist() {
        ResponseEntity<Void> resp = testRestTemplate.exchange("http://localhost:" + port +
                "/api/v1/reviews/100", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}