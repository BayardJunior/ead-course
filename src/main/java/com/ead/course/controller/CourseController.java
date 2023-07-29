package com.ead.course.controller;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Validated CourseDto courseDto) {

        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourseById(@PathVariable(value = "courseId") UUID courseId) {

        if (!this.courseService.existsById(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        CourseModel courseModel = this.courseService.findCourseById(courseId);
        this.courseService.cascadeDeleteSafety(courseModel);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course deleted sucessfully");
    }

    @PutMapping("{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Validated CourseDto courseDto) {

        if (!this.courseService.existsById(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        CourseModel courseModel = this.courseService.findCourseById(courseId);

        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> findAllCourses() {

        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.findAllCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> findCourseById(@PathVariable(value = "courseId") UUID courseId) {

        if (!this.courseService.existsById(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.findCourseById(courseId));
    }
}
