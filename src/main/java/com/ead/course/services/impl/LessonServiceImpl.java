package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    LessonRepository lessonRepository;

    @Override
    public void cascadeDeleteSafety(UUID moduleId) {
        List<LessonModel> allLessonsIntoModule = findAllLessonsIntoModule(moduleId);
        if (!allLessonsIntoModule.isEmpty()) {
            deleteAll(allLessonsIntoModule);
        }
    }

    @Override
    public List<LessonModel> findAllLessonsIntoModule(UUID moduleId) {
        return lessonRepository.findAllLessonsIntoModule(moduleId);
    }

    @Override
    public void deleteAll(List<LessonModel> allLessonsIntoModule) {
        lessonRepository.deleteAll(allLessonsIntoModule);
    }

    @Override
    public LessonModel save(LessonModel lesson) {
        return this.lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(LessonModel lesson) {
        this.lessonRepository.delete(lesson);
    }

    @Override
    public Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId) {
        return this.lessonRepository.findLessonIntoModule(moduleId, lessonId);
    }

    @Override
    public Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable) {
        return this.lessonRepository.findAll(spec, pageable);
    }

}
