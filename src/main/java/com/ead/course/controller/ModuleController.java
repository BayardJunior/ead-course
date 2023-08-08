package com.ead.course.controller;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
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

        log.debug("Post saveModule moduleDto received {}", moduleDto.toString());
        if (!this.courseService.existsById(courseId)) {
            log.warn("POST saveModule courseId {} not found", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        CourseModel course = this.courseService.findCourseById(courseId);

        ModuleModel moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(course);

        this.moduleService.save(moduleModel);
        log.debug("POST saveModule getModuleId {}", moduleModel.getModuleId());
        log.info("Module modlueId {} saved successfully!", moduleModel.getModuleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleModel);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Object> deleteModuleById(@PathVariable(value = "courseId") UUID courseId,
                                                   @PathVariable(value = "moduleId") UUID moduleId) {

        log.debug("DELETE deleteModuleById courseId {} moduleId {} received", courseId, moduleId);
        Optional<ModuleModel> module = this.moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!module.isPresent()) {
            log.warn("DELETE deleteModuleById Module {} Not Found For This Course {}!", moduleId, courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found For This Course!");
        }
        this.moduleService.cascadeDeleteSafety(module.get());
        log.debug("DELETE deleteModuleById moduleId deleted {}", moduleId);
        log.info("Module moduleId {} deleted!", moduleId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module deleted sucessfully");
    }

    @PutMapping("{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Validated ModuleDto moduleDto) {

        log.debug("PUT updateModule courseId {} moduleId {} received", courseId, moduleId);
        Optional<ModuleModel> module = this.moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!module.isPresent()) {
            log.warn("PUT updateModule Module {} Not Found For This Course {}!", moduleId, courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found For This Course!");
        }

        BeanUtils.copyProperties(moduleDto, module.get());
        log.debug("PUT updateModule moduleId updated {}", moduleId);
        log.info("Module moduleId {} updated!", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(this.moduleService.save(module.get()));
    }

    @GetMapping
    public ResponseEntity<Page<ModuleModel>> findAllModules(@PathVariable(value = "courseId") UUID courseId,
                                                            SpecificationTemplate.ModuleSpec spec,
                                                            @PageableDefault(page = 0, size = 10, sort = "moduleId",
                                                                    direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.moduleService.findAllModulesIntoCourse(
                SpecificationTemplate.moduleCourseIdSpec(courseId).and(spec), pageable));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Object> findModuleById(@PathVariable(value = "courseId") UUID courseId,
                                                 @PathVariable(value = "moduleId") UUID moduleId) {

        log.debug("GET findModuleById courseId {} moduleId {} received", courseId, moduleId);
        Optional<ModuleModel> module = this.moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!module.isPresent()) {
            log.warn("GET findModuleById Module {} Not Found For This Course {}!", moduleId, courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found For This Course!");
        }
        log.debug("GET findModuleById moduleId {}", moduleId);
        log.info("Lesson moduleId {} found!", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(module.get());
    }
}
