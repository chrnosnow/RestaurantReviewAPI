package com.codecademy.RestaurantReviewAPI.enums;

import jakarta.persistence.AttributeConverter;

public class ReviewStatusConverter implements AttributeConverter<ReviewStatus, String> {
    @Override
    public String convertToDatabaseColumn(ReviewStatus reviewStatus) {
        return switch (reviewStatus){
            case UNSUBMITTED -> "u";
            case PENDING -> "p";
            case APPROVED -> "a";
            case REJECTED -> "r";
        };
    }

    @Override
    public ReviewStatus convertToEntityAttribute(String s) {
        return switch (s){
            case "u" -> ReviewStatus.UNSUBMITTED;
            case "p" -> ReviewStatus.PENDING;
            case "a" -> ReviewStatus.APPROVED;
            case "r" -> ReviewStatus.REJECTED;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
