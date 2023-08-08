package com.ead.course.infrastructure.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SubscriptionUserInCourseDto {

    private UUID userId;
    private UUID courseId;
}
