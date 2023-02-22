package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.DiningReviewApiApplication;
import com.codecademy.RestaurantReviewAPI.entities.User;
import com.codecademy.RestaurantReviewAPI.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DiningReviewApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    private RestTemplate patchRestTemplate;


    @Test
    public void whenMakingAPostUserIsSaved() {
        //setup
        User user = new User();
        user.setName("JoeBatman");
        user.setCity("London");

        //execute
        ResponseEntity<User> userResponseEntity = this.testRestTemplate.postForEntity("http://localhost:" + port +
                "/api/v1/users", user, User.class);

        //validate
        assertEquals(HttpStatus.CREATED, userResponseEntity.getStatusCode());
        assertEquals(user.getName(), Objects.requireNonNull(userResponseEntity.getBody()).getName());
        assertEquals(user.getCity(), userResponseEntity.getBody().getCity());
        assertNotNull(userResponseEntity.getBody().getId());
    }

    @Test
    public void shouldGetListOfUsers() {
        ResponseEntity<User[]> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port + "/api" +
                "/v1/users", User[].class);
        User[] allUsers = responseEntity.getBody();

        assertEquals(2, allUsers.length);
    }

    @Test
    public void shouldGetUserIfGivenValidName() {
        ResponseEntity<User> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port + "/api" +
                "/v1/users/user/JonLee", User.class);
        String username = responseEntity.getBody().getName();
        assertEquals("JonLee", responseEntity.getBody().getName());
    }

    @Test
    public void shouldNotGetUserIfGivenInvalidName() {
        ResponseEntity<User> responseEntity = this.testRestTemplate.getForEntity("http://localhost:" + port + "/api" +
                "/v1/users/user/JonL", User.class);
        String username = responseEntity.getBody().getName();
        assertEquals(null, responseEntity.getBody().getName());
    }

    @Test
    public void shouldDeleteUserIfExists() {
        ResponseEntity<Void> resp = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/users/1",
                HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void shouldNotDeleteUserIfDoesNotExist() {
        ResponseEntity<Void> resp = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/users/10",
                HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    public void shouldUpdateUser() {
        String url = "http://localhost:" + port + "/api/v1/users/JonLee";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON);

        String patchString = "[{\n" +
                "    \"op\":\"replace\",\n" +
                "    \"path\": \"/city\",\n" +
                "    \"value\":\"London\"\n" +
                "}]";

        HttpEntity<String> requestEntity = new HttpEntity<String>(patchString, reqHeaders);
        ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, User.class);
        User result = responseEntity.getBody();

        assertEquals("London", result.getCity());
    }
}