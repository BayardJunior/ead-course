package com.ead.course.services.impl;

import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonService lessonService;

    @Transactional
    @Override
    public void cascadeDeleteSafety(ModuleModel module) {
        lessonService.cascadeDeleteSafety(module.getModuleId());
        moduleRepository.delete(module);
    }

    @Override
    public void cascadeDeleteSafety(List<ModuleModel> moduleList) {
        moduleList.forEach(moduleModel -> lessonService.cascadeDeleteSafety(moduleModel.getModuleId()));
        moduleRepository.deleteAll(moduleList);
    }

    @Override
    public List<ModuleModel> findAllModulesIntoCourse(UUID courseId) {
        return moduleRepository.findAllModulesIntoCourse(courseId);
    }

    @Override
    public ModuleModel save(ModuleModel module) {
        return this.moduleRepository.save(module);
    }

    @Override
    public boolean existsById(UUID moduleId) {
        return this.moduleRepository.existsById(moduleId);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findModuleIntoCourse(courseId, moduleId);
    }

    @Override
    public List<ModuleModel> findAllModules() {
        return this.moduleRepository.findAll();
    }

    @Override
    public Optional<ModuleModel> findModuleById(UUID courseId) {
        return this.moduleRepository.findById(courseId);
    }

    @Override
    public Page<ModuleModel> findAllModulesIntoCourse(Specification<ModuleModel> spec, Pageable pageable) {
        return this.moduleRepository.findAll(spec, pageable);
    }
}
