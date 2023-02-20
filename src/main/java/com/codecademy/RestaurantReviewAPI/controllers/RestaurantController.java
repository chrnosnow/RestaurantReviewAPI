package com.codecademy.RestaurantReviewAPI.controllers;

import com.codecademy.RestaurantReviewAPI.entities.Restaurant;
import com.codecademy.RestaurantReviewAPI.repositories.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/restaurants")
public class RestaurantController {
    private RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public Iterable<Restaurant> getAllRestaurants() {
        return this.restaurantRepository.findAllByOrderByNameAsc();
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable("id") Long id) {
        Optional<Restaurant> restaurantOptional = this.restaurantRepository.findById(id);
        if (!restaurantOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return restaurantOptional.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant addRestaurant(@RequestBody Restaurant newRestaurant) {
        if (ObjectUtils.isEmpty(newRestaurant.getName()) || ObjectUtils.isEmpty(newRestaurant.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (this.restaurantRepository.findByNameAndCodeIgnoreCase(newRestaurant.getName(), newRestaurant.getCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restaurant with given name and zip code already " +
                    "exists.");
        }

        return (Restaurant) this.restaurantRepository.save(newRestaurant);
    }

    @GetMapping("/search")
    public List<Restaurant> searchRestaurant(@RequestParam(name = "code") String code,
                                             @RequestParam(name = "allergy") String allergy) {

        List<Restaurant> list = null;
        if (Objects.nonNull(code)) {
            if (Objects.nonNull(allergy)) {
                switch (allergy.toLowerCase()) {
                    case "peanut":
                        list = this.restaurantRepository.findByCodeAndPeanutAvgScoreIsNotNullOrderByPeanutAvgScoreDesc(code);
                        break;
                    case "egg":
                        list = this.restaurantRepository.findByCodeAndEggAvgScoreIsNotNullOrderByEggAvgScoreDesc(code);
                        break;
                    case "dairy":
                        list = this.restaurantRepository.findByCodeAndDairyAvgScoreIsNotNullOrderByDairyAvgScoreDesc(code);
                        break;
                    default:
                        list = new ArrayList<>();
                }
            }
        }
        return list;
    }

}
