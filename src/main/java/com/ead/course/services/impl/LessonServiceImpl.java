package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
