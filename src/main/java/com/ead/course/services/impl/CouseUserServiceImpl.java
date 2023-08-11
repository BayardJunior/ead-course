package com.ead.course.services.impl;

import com.ead.course.infrastructure.components.AuthUserComponentImpl;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CouseUserServiceImpl implements CourseUserService {

    @Autowired
    CourseUserRepository repository;

    @Autowired
    AuthUserComponentImpl userComponent;

    @Override
    public boolean existsUserIdInCourse(CourseModel course, UUID userId) {

        return this.repository.existsByCourseAndUserId(course, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return this.repository.save(courseUserModel);
    }

    @Transactional
    @Override
    public CourseUserModel saveAndSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        CourseUserModel saved = this.repository.save(courseUserModel);

        this.userComponent.sendSubscpritionToAuthUser(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId());

        return saved;
    }

    @Override
    public void deleteAllCourseUsersByCourse(CourseModel courseModel) {
        List<CourseUserModel> coursesUsersIntoCourse = this.repository.findAllCourseUserModelIntoCourse(courseModel.getCourseId());
        this.repository.deleteAll(coursesUsersIntoCourse);

    }
}
