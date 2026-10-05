package org.example.spring_crud_activity.service;
import org.example.spring_crud_activity.exception.ActivityNotFoundException;
import org.example.spring_crud_activity.domain.ClubActivity;
import org.example.spring_crud_activity.dto.ClubActivityRequest;
import org.example.spring_crud_activity.dto.ClubActivityResponse;
import org.example.spring_crud_activity.repository.ClubActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubActivityService {

    private final ClubActivityRepository repository;

    public ClubActivityService(ClubActivityRepository repository) {
        this.repository = repository;
    }

    public ClubActivityResponse create(ClubActivityRequest request) {
        validate(request);

        ClubActivity activity = new ClubActivity(
                null,
                request.title(),
                request.clubName(),
                request.category(),
                request.activityDate(),
                request.location(),
                request.participants(),
                request.description()
        );

        ClubActivity saved = repository.save(activity);

        return toResponse(saved);
    }


    public List<ClubActivityResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public ClubActivityResponse findById(Long id) {
        ClubActivity activity = repository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        return toResponse(activity);
    }


    public ClubActivityResponse update(Long id, ClubActivityRequest request) {
        validate(request);

        repository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        ClubActivity activity = new ClubActivity(
                id,
                request.title(),
                request.clubName(),
                request.category(),
                request.activityDate(),
                request.location(),
                request.participants(),
                request.description()
        );

        ClubActivity updated = repository.update(id, activity);

        return toResponse(updated);
    }


    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        repository.delete(id);
    }

    public List<ClubActivityResponse> findByCategory(String category) {
        return repository.findByCategory(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private void validate(ClubActivityRequest request) {

        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("활동 제목은 필수입니다.");
        }

        if (request.activityDate() == null || request.activityDate().isBlank()) {
            throw new IllegalArgumentException("활동 날짜는 필수입니다.");
        }

        if (request.participants() == null || request.participants() < 1) {
            throw new IllegalArgumentException("참여 인원은 1명 이상이어야 합니다.");
        }
    }

    private ClubActivityResponse toResponse(ClubActivity activity) {
        return new ClubActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getClubName(),
                activity.getCategory(),
                activity.getActivityDate(),
                activity.getLocation(),
                activity.getParticipants(),
                activity.getDescription()
        );
    }
}