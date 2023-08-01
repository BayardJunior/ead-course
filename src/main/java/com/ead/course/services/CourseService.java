package com.ead.course.services;

import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CourseService {

    void cascadeDeleteSafety(CourseModel courseModel);

    CourseModel save(CourseModel courseModel);

    boolean existsById(UUID courseId);

    CourseModel findCourseById(UUID courseId);

    Page<CourseModel> findAllCourses(Specification<CourseModel> courseSpec, Pageable pageable);
}
