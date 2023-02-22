package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.DiningReviewApiApplication;
import com.codecademy.RestaurantReviewAPI.entities.Restaurant;
import com.codecademy.RestaurantReviewAPI.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DiningReviewApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    void shouldGetAllRestaurants() {
        ResponseEntity<Restaurant[]> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port +
                "/api/v1/restaurants", Restaurant[].class);
        Restaurant[] allRestaurants = responseEntity.getBody();

        assertEquals(2, allRestaurants.length);
    }

    @Test
    void shouldGetRestaurantById() {
        ResponseEntity<Restaurant> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port +
                "/api/v1/restaurants/2", Restaurant.class);
        Restaurant restaurant = responseEntity.getBody();

        assertEquals("Bubble Tea Joy", restaurant.getName());
        assertEquals("100-0102", restaurant.getCode());
    }

    @Test
    void addRestaurant() {
        Restaurant newRest = new Restaurant();
        newRest.setName("B2");
        newRest.setCode("100-0102");

        ResponseEntity<Restaurant> restResponseEntity = this.testRestTemplate.postForEntity("http://localhost:" + port +
                "/api/v1/reviews", newRest, Restaurant.class);
        Restaurant restaurant = restResponseEntity.getBody();

        assertEquals(HttpStatus.CREATED, restResponseEntity.getStatusCode());
        assertEquals(newRest.getName(), restaurant.getName());
    }

    @Test
    public void shouldSearchForRestaurantByGivenDetails() {
        ResponseEntity<Restaurant[]> restResponseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port +
                "/api/v1/restaurants/search?code=700500&allergy=egg", Restaurant[].class);
        Restaurant[] restaurants = restResponseEntity.getBody();

        assertEquals(1, restaurants.length);
    }
}