package com.codecademy.RestaurantReviewAPI.entities;

import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusAdmin {
    private ReviewStatus status;
}
