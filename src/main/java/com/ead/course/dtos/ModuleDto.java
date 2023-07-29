package com.ead.course.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ModuleDto {

    @NotBlank
    public String title;

    @NotBlank
    public String description;
}
