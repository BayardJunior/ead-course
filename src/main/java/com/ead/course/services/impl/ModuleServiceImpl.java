package com.ead.course.services.impl;

import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
}
