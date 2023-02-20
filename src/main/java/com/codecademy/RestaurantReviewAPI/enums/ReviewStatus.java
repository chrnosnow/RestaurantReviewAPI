package com.codecademy.RestaurantReviewAPI.enums;

public enum ReviewStatus {
    UNSUBMITTED,
    PENDING,
    APPROVED,
    REJECTED

//    private String shortName;
//
//    private ReviewStatus(String shortName) {
//        this.shortName = shortName;
//    }
//
//    public String getShortName() {
//        return shortName;
//    }
//
//    public static ReviewStatus fromShortName(String shortName) {
//        switch (shortName) {
//            case "U":
//                return ReviewStatus.UNSUBMITTED;
//            case "P":
//                return ReviewStatus.PENDING;
//            case "A":
//                return ReviewStatus.APPROVED;
//            case "R":
//                return ReviewStatus.REJECTED;
//            default:
//                throw new IllegalArgumentException("ShortName [" + shortName + "] not supported.");
//        }
//    }
}
