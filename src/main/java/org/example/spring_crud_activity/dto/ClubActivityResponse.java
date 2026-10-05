package org.example.spring_crud_activity.dto;

public record ClubActivityResponse(
        Long id,
        String title,
        String clubName,
        String category,
        String activityDate,
        String location,
        Integer participants,
        String description
) {
}