package com.ead.course.controller;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    CourseService courseService;

    @Autowired
    UserService service;

    @GetMapping
    public ResponseEntity<Object> findAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                       @PathVariable(value = "courseId") UUID courseId) {
        if (!this.courseService.existsById(courseId)) {
            log.warn("POST saveSubscrptionUserInCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("T");
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscrptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                              @RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("POST saveSubscrptionUserInCourse courseId received {}", courseId);
        if (!this.courseService.existsById(courseId)) {
            log.warn("POST saveSubscrptionUserInCourse courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        // TODO: 25/08/2023 validaçoes de state transfer
        return ResponseEntity.status(HttpStatus.CREATED).body("T");
    }

}
