package com.ead.course.services;

import com.ead.course.models.CourseModel;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    void cascadeDeleteSafety(CourseModel courseModel);

    CourseModel save(CourseModel courseModel);

    boolean existsById(UUID courseId);

    CourseModel findCourseById(UUID courseId);

    List<CourseModel> findAllCourses();
}
