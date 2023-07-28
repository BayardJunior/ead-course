package com.ead.course.services;

import com.ead.course.models.ModuleModel;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    void cascadeDeleteSafety(ModuleModel module);

    void cascadeDeleteSafety(List<ModuleModel> moduleList);

    List<ModuleModel> findAllModulesIntoCourse(UUID courseId);
}
