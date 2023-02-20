package com.codecademy.RestaurantReviewAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="RESTAURANTS")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="NAME")
    private String name;
    @Column(name="CITY")
    private String city;
    @Column(name="STATE")
    private String state;
    @Column(name="ZIPCODE",columnDefinition = "VARCHAR(10)", nullable = false)
    private String code;
    @Column(name = "PEANUT_AVG_SCORE")
    private Double peanutAvgScore;
    @Column(name = "EGG_AVG_SCORE")
    private Double eggAvgScore;
    @Column(name = "DAIRY_AVG_SCORE")
    private Double dairyAvgScore;
    @Column(name = "AVG_SCORE")
    private Double averageScore;
}
