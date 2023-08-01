package com.ead.course.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LessonDto {

    @NotBlank
    public String title;

    public String description;

    @NotBlank
    public String videoUrl;
}
