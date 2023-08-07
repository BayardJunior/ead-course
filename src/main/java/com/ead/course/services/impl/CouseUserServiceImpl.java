package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CouseUserServiceImpl implements CourseUserService {

    @Autowired
    CourseUserRepository repository;

    @Override
    public boolean existsUserIdInCourse(CourseModel course, UUID userId) {

        return this.repository.existsByCourseAndUserId(course, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return this.repository.save(courseUserModel);
    }
}
