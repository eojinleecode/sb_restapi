package org.example.spring_crud_activity.exception;

public class ActivityNotFoundException extends RuntimeException {

    public ActivityNotFoundException(Long id) {
        super("해당 활동을 찾을 수 없습니다. id=" + id);
    }
}