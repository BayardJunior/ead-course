package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void cascadeDeleteSafety(ModuleModel module);

    void cascadeDeleteSafety(List<ModuleModel> moduleList);

    List<ModuleModel> findAllModulesIntoCourse(UUID courseId);

    ModuleModel save(ModuleModel module);

    boolean existsById(UUID moduleId);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    List<ModuleModel> findAllModules();

    Optional<ModuleModel> findModuleById(UUID courseId);

    Page<ModuleModel> findAllModulesIntoCourse(Specification<ModuleModel> spec, Pageable pageable);

    void deleteAllModulesByCourse(CourseModel courseModel);
}
