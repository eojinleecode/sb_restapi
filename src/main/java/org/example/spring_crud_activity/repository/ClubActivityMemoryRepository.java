package org.example.spring_crud_activity.repository;

import org.example.spring_crud_activity.domain.ClubActivity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ClubActivityMemoryRepository implements ClubActivityRepository {

    private final Map<Long, ClubActivity> activities = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public ClubActivity save(ClubActivity activity) {
        Long id = sequence.incrementAndGet();
        activity.setId(id);
        activities.put(id, activity);
        return activity;
    }

    @Override
    public List<ClubActivity> findAll() {
        return new ArrayList<>(activities.values());
    }

    @Override
    public Optional<ClubActivity> findById(Long id) {
        return Optional.ofNullable(activities.get(id));
    }

    @Override
    public ClubActivity update(Long id, ClubActivity activity) {
        activity.setId(id);
        activities.put(id, activity);
        return activity;
    }

    @Override
    public void delete(Long id) {
        activities.remove(id);
    }

    @Override
    public List<ClubActivity> findByCategory(String category) {
        return activities.values().stream()
                .filter(activity -> activity.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}