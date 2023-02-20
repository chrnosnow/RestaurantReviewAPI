package com.codecademy.RestaurantReviewAPI.entities;

import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
import com.codecademy.RestaurantReviewAPI.enums.ReviewStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ADMIN_REVIEWS")
@Getter
@Setter
public class AdminReviewAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column (name = "ADMIN_ID", nullable = false)
    private Long adminId;
    @Column(name="REVIEW_ID")
    private Long reviewId;
    @Column(name = "REVIEW_STATUS")
    @Convert(converter = ReviewStatusConverter.class)
    private ReviewStatus reviewStatus;
}
