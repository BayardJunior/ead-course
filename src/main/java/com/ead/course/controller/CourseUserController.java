package com.ead.course.controller;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.infrastructure.components.AuthUserComponentImpl;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping()
public class CourseUserController {


    @Autowired
    AuthUserComponentImpl userComponent;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseUserService service;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> findAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                       @PathVariable(value = "courseId") UUID courseId) {
        if (!this.courseService.existsById(courseId)) {
            log.warn("GET findAllUsersByCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userComponent.findAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscrptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                              @RequestBody @Valid SubscriptionDto subscriptionDto) {
        ResponseEntity<UserDto> user = null;
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

        try {
            user = userComponent.findUsersById(subscriptionDto.getUserId());
            if (user.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User Is Blocked!");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
            }
        }

        //CourseUserModel courseUserModel = this.service.save(course.convertToCourseUserModel(subscriptionDto.getUserId()));
        CourseUserModel courseUserModel = this.service.saveAndSubscriptionUserInCourse(course.convertToCourseUserModel(subscriptionDto.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

    @DeleteMapping("courses/users/{userId}")
    public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId) {
        if (!this.service.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser Not Found!");
        }
        this.service.deleteCourseByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfuly!");
    }
}