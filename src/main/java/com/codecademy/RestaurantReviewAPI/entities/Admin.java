package com.codecademy.RestaurantReviewAPI.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMINS")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "NAME")
    String name;
}
