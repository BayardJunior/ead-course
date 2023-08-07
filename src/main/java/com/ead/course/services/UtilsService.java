package com.ead.course.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {

    String getUrlToAllUsersByCourseId(UUID courseId, Pageable pageable);
    String getUrlToFindUserById(UUID userId);
}
