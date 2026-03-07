package com.hasithmalshan.confession_form.model.enums;

public enum PostCategory {
    ACADEMIC("Academic"),
    CAMPUS_LIFE("Campus Life"),
    SOCIAL("Social & Friends"),
    CAREER("Career & Work"),
    WELLNESS("Wellness"),
    HUMOR("Humor & Relatable"),
    ACHIEVEMENTS("Achievements"),
    RECOMMENDATIONS("Recommendations"),
    PERSONAL_GROWTH("Personal Growth"),
    GENERAL("General");

    private final String displayName;

    PostCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}