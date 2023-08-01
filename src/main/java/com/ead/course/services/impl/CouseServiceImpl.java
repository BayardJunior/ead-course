package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

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

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public boolean existsById(UUID courseId) {
        return courseRepository.existsById(courseId);
    }

    @Override
    public CourseModel findCourseById(UUID courseId) {
        return courseRepository.findById(courseId).get();
    }

    @Override
    public Page<CourseModel> findAllCourses(Specification<CourseModel> courseSpec, Pageable pageable) {
        return courseRepository.findAll(courseSpec, pageable);
    }

    private void deleteAllModulesByCourse(CourseModel courseModel) {
        List<ModuleModel> allModulesIntoCourse = moduleService.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!allModulesIntoCourse.isEmpty()) {
            moduleService.cascadeDeleteSafety(allModulesIntoCourse);
        }
    }
}
