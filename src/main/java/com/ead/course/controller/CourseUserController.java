package com.ead.course.controller;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.infrastructure.components.UserComponentImpl;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/courses/{courseId}/users")
public class CourseUserController {


    @Autowired
    UserComponentImpl userComponent;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseUserService service;

    @GetMapping
    public ResponseEntity<Page<UserDto>> findAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                              @PathVariable(value = "courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK).body(userComponent.findAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscrptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                              @RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("POST saveSubscrptionUserInCourse courseId received {}", courseId);
        if (!this.courseService.existsById(courseId)) {
            log.warn("POST saveSubscrptionUserInCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        CourseModel course = this.courseService.findCourseById(courseId);
        log.debug("POST saveSubscrptionUserInCourse courseId {}", courseId);
        log.info("Course userId {} found!", courseId);
        if (this.service.existsUserIdInCourse(course, subscriptionDto.getUserId())) {
            log.error("User {} already subscripted for this course {}!", subscriptionDto.getUserId(), courseId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        // TODO: 07/08/2023 Validação de user ativo

        CourseUserModel courseUserModel = this.service.save(course.convertToCourseUserModel(subscriptionDto.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully!");
    }

}