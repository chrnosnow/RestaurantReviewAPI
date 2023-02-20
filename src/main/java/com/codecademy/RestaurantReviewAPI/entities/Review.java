package com.codecademy.RestaurantReviewAPI.entities;

import com.codecademy.RestaurantReviewAPI.enums.ReviewStatus;
//import com.codecademy.RestaurantReviewAPI.enums.ReviewStatusConverter;
import com.codecademy.RestaurantReviewAPI.enums.ReviewStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "REVIEWS")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USERNAME", updatable = false, nullable = false)
    private String userName;
    @Column(name = "RESTAURANTID", nullable = false)
    private Long restaurantId;
    @Column(name = "PEANUT_SCORE")
    private Integer peanutScore;
    @Column(name = "EGG_SCORE")
    private Integer eggScore;
    @Column(name = "DAIRY_SCORE")
    private Integer dairyScore;
    @Column(name = "COMMENT", length = 1500)
    private String comment;
//    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    @Convert(converter = ReviewStatusConverter.class)
    private ReviewStatus status = ReviewStatus.UNSUBMITTED;
}
