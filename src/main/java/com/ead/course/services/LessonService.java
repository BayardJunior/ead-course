package com.ead.course.services;

import com.ead.course.models.LessonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    void cascadeDeleteSafety(UUID moduleId);

    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

    void deleteAll(List<LessonModel> allLessonsIntoModule);

    LessonModel save(LessonModel lesson);

    void deleteLesson(LessonModel lesson);

    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

    Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable);
}
