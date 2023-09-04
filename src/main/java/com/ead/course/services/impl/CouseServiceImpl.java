package com.ead.course.services.impl;

import com.ead.course.infrastructure.event.dtos.NotificationCommandDto;
import com.ead.course.infrastructure.event.publishers.NotificationCommandPublisher;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Log4j2
@Service
public class CouseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleService moduleService;

    @Autowired
    UserService userService;

    @Autowired
    NotificationCommandPublisher notificationCommandPublisher;

    @Transactional
    @Override
    public void cascadeDeleteSafety(CourseModel courseModel) {
        moduleService.deleteAllModulesByCourse(courseModel);
        this.deleteCourseUserByCourseId(courseModel.getCourseId());
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public boolean existsById(UUID courseId) {
        return courseRepository.existsById(courseId);
    }

    @Override
    public CourseModel findCourseById(UUID courseId) {
        return courseRepository.findById(courseId).get();
    }

    @Override
    public Page<CourseModel> findAllCourses(Specification<CourseModel> courseSpec, Pageable pageable) {
        return courseRepository.findAll(courseSpec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {

        return this.courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserinCourse(UUID courseId, UUID userId) {
        this.courseRepository.saveSubscriptionUserinCourse(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserinCourseAndSendNotification(CourseModel courseModel, UserModel userModel) {
        this.saveSubscriptionUserinCourse(courseModel.getCourseId(), userModel.getUserId());
        try {
            NotificationCommandDto notificationCommandDto = new NotificationCommandDto();
            notificationCommandDto.setTitle("Seja bem vindo(a) ao curso: ".concat(courseModel.getName()));
            notificationCommandDto.setMessage(userModel.getFullName().concat("a sua matricula foi realizada com sucesso!"));
            notificationCommandDto.setUserId(userModel.getUserId());
            notificationCommandPublisher.publichNotificationCommand(notificationCommandDto);
        } catch (Exception e) {
            log.warn("Error sending notification!");
        }
    }


    @Override
    public void deleteCourseUserByUserId(UUID userId) {
        this.courseRepository.deleteCourseUserByUserId(userId);
    }

    private void deleteCourseUserByCourseId(UUID courseId) {
        this.courseRepository.deleteCourseUserByCourseId(courseId);
    }
}
