package org.example.spring_crud_activity.dto;

public record ClubActivityRequest(
        String title,
        String clubName,
        String category,
        String activityDate,
        String location,
        Integer participants,
        String description
) {
}