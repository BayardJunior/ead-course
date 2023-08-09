package com.ead.course.controller;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validator.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseValidator validator;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Validated CourseDto courseDto, Errors errors) {

        log.debug("Post saveCourse courseDto received {}", courseDto.toString());
        validator.validate(courseDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        this.courseService.save(courseModel);
        log.debug("Post saveCourse courseId saved {}", courseModel.getCourseId());
        log.info("Course saved successfully courseId {}", courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourseById(@PathVariable(value = "courseId") UUID courseId) {

        log.debug("DELETE deleteUser courseId received {}", courseId);
        if (!this.courseService.existsById(courseId)) {
            log.warn("DELETE deleteUser userId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        CourseModel courseModel = this.courseService.findCourseById(courseId);
        this.courseService.cascadeDeleteSafety(courseModel);
        log.debug("DELETE deleteCourseById courseId deleted {}", courseId);
        log.info("Course courseId {} deleted!", courseId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course deleted successfully");
    }

    @PutMapping("{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Validated CourseDto courseDto) {
        log.debug("PUT updateCourse courseId received {}", courseId);
        if (!this.courseService.existsById(courseId)) {
            log.warn("PUT updateCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        CourseModel courseModel = this.courseService.findCourseById(courseId);

        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        log.debug("PUT updateCourse courseId updated {}", courseId);
        log.info("Course courseId {} deleted!", courseId);
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> findAllCourses(SpecificationTemplate.CourseSpec courseSpec,
                                                            @PageableDefault(page = 0, size = 10, sort = "courseId",
                                                                    direction = Sort.Direction.ASC) Pageable pageable,
                                                            @RequestParam(required = false) UUID userId) {

        Page<CourseModel> page;
        if (userId != null) {
            page = this.courseService.findAllCourses(SpecificationTemplate.courseUserIdSpec(userId).and(courseSpec), pageable);
        } else {
            page = this.courseService.findAllCourses(courseSpec, pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> findCourseById(@PathVariable(value = "courseId") UUID courseId) {

        log.debug("GET findCourseById courseId received {}", courseId);
        if (!this.courseService.existsById(courseId)) {
            log.warn("GET findCourseById courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        log.debug("GET findCourseById courseId {}", courseId);
        log.info("Course userId {} found!", courseId);
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.findCourseById(courseId));
    }
}
