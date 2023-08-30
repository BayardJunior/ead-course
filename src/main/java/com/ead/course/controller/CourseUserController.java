package com.ead.course.controller;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/courses/{courseId}/users")
public class CourseUserController {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Object> findAllUsersByCourse(SpecificationTemplate.UserSpec userSpec,
                                                       @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                       @PathVariable(value = "courseId") UUID courseId) {
        if (!this.courseService.existsById(courseId)) {
            log.warn("POST saveSubscrptionUserInCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(SpecificationTemplate.userCourseIdSpec(courseId).and(userSpec), pageable));
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscrptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                              @RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("POST saveSubscrptionUserInCourse courseId received {}", courseId);
        if (!this.courseService.existsById(courseId)) {
            log.warn("POST saveSubscrptionUserInCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        Optional<UserModel> userModel = this.userService.findById(subscriptionDto.getUserId());
        if (!userModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        }

        if (userModel.get().getUserStatus().equals(UserStatus.BLOCKED)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Is Blocked!");
        }

        if (this.courseService.existsByCourseAndUser(courseId, subscriptionDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Subscription already exists!");
        }

        CourseModel courseById = this.courseService.findCourseById(courseId);
        courseService.saveSubscriptionUserinCourse(courseById.getCourseId(), userModel.get().getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription Created Successfully!");
    }

}
