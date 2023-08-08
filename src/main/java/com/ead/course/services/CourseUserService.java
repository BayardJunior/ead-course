package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;

import java.util.UUID;

public interface CourseUserService {

    boolean existsUserIdInCourse(CourseModel course, UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);

    CourseUserModel saveAndSubscriptionUserInCourse(CourseUserModel courseUserModel);
}
