package com.ead.course.controller;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    CourseService courseService;

    @Autowired
    ModuleService moduleService;

    @PostMapping()
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Validated ModuleDto moduleDto) {

        if (!this.courseService.existsById(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        CourseModel course = this.courseService.findCourseById(courseId);

        ModuleModel moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(course);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.moduleService.save(moduleModel));
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Object> deleteModuleById(@PathVariable(value = "courseId") UUID courseId,
                                                   @PathVariable(value = "moduleId") UUID moduleId) {

        Optional<ModuleModel> module = this.moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!module.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found For This Course!");
        }
        this.moduleService.cascadeDeleteSafety(module.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module deleted sucessfully");
    }

    @PutMapping("{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Validated ModuleDto moduleDto) {
        Optional<ModuleModel> module = this.moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!module.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found For This Course!");
        }

        BeanUtils.copyProperties(moduleDto, module.get());

        return ResponseEntity.status(HttpStatus.OK).body(this.moduleService.save(module.get()));
    }

    @GetMapping
    public ResponseEntity<List<ModuleModel>> findAllModules(@PathVariable(value = "courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK).body(this.moduleService.findAllModulesIntoCourse(courseId));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Object> findModuleById(@PathVariable(value = "courseId") UUID courseId,
                                                 @PathVariable(value = "moduleId") UUID moduleId) {

        Optional<ModuleModel> module = this.moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!module.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found For This Course!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(module.get());
    }
}
