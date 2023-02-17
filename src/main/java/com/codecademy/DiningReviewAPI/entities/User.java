package com.codecademy.DiningReviewAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, updatable = false, nullable = false)
    private String name;
    private String city;
    private String state;
    private String code;
    @Column(name="PEANUT_ALLERGY")
    private Boolean peanutAllergy;
    @Column(name="EGG_ALLERGY")
    private Boolean eggAllergy;
    @Column(name="DAIRY_ALLERGY")
    private Boolean dairyAllergy;
}
