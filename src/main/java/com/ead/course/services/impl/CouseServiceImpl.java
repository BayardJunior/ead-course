package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CouseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleService moduleService;

    @Transactional
    @Override
    public void cascadeDeleteSafety(CourseModel courseModel) {
        deleteAllModulesByCourse(courseModel);
        courseRepository.delete(courseModel);
    }

    private void deleteAllModulesByCourse(CourseModel courseModel) {
        List<ModuleModel> allModulesIntoCourse = moduleService.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!allModulesIntoCourse.isEmpty()) {
            moduleService.cascadeDeleteSafety(allModulesIntoCourse);
        }
    }
}
