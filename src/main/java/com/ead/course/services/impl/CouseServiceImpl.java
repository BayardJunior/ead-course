package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class CouseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleService moduleService;

    @Autowired
    UserService userService;

    @Transactional
    @Override
    public void cascadeDeleteSafety(CourseModel courseModel) {
        moduleService.deleteAllModulesByCourse(courseModel);
        this.deleteCourseUserByCourseId(courseModel.getCourseId());
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

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {

        return this.courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserinCourse(UUID courseId, UUID userId) {
        this.courseRepository.saveSubscriptionUserinCourse(courseId, userId);
    }

    @Override
    public void deleteCourseUserByUserId(UUID userId) {
        this.courseRepository.deleteCourseUserByUserId(userId);
    }

    private void deleteCourseUserByCourseId(UUID courseId) {
        this.courseRepository.deleteCourseUserByCourseId(courseId);
    }
}
