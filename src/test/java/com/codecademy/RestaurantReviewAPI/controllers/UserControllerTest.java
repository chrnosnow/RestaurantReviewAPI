package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.DiningReviewApiApplication;
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
class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void whenMakingAPostUserIsSaved(){
        //setup
        User user = new User();
        user.setName("JoeBatman");
        user.setCity("London");

        //execute
        ResponseEntity<User> userResponseEntity = this.testRestTemplate.postForEntity("http://localhost:" + port +
                "/api/v1/users", user, User.class);

        //validate
        assertEquals(HttpStatus.CREATED, userResponseEntity.getStatusCode());
        assertEquals(user.getName(), userResponseEntity.getBody().getName());
        assertEquals(user.getCity(), userResponseEntity.getBody().getCity());
        assertNotNull(userResponseEntity.getBody().getId());
    }
}