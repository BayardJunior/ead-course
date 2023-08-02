package com.ead.course.controller;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
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
@RequestMapping("/modules/{moduleId}/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @PostMapping()
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID modulesId,
                                             @RequestBody @Validated LessonDto lessonDto) {

        log.debug("Post saveLesson lessonDto received {}", lessonDto.toString());
        if (!this.moduleService.existsById(modulesId)) {
            log.warn("POST saveLesson modulesId {} not found", modulesId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found!");
        }

        ModuleModel moduleModel = this.moduleService.findModuleById(modulesId).get();

        LessonModel lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);

        this.lessonService.save(lessonModel);
        log.debug("POST saveLesson lessonDto {}", lessonModel.toString());
        log.info("Lesson lessonId {} saved successfully!", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonModel);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Object> deleteLessonById(@PathVariable(value = "moduleId") UUID moduleId,
                                                   @PathVariable(value = "lessonId") UUID lessonId) {

        log.debug("DELETE deleteModuleById moduleId {} and lessonId {} received ", moduleId, lessonId);
        Optional<ModuleModel> module = this.moduleService.findModuleById(moduleId);
        if (!module.isPresent()) {
            log.warn("DELETE deleteModuleById moduleId {} not found", moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found!");
        }
        Optional<LessonModel> lesson = this.lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!lesson.isPresent()) {
            log.warn("DELETE deleteModuleById Lesson {} Not Found For This Module {}", lessonId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found For This Module!");
        }
        this.lessonService.deleteLesson(lesson.get());
        log.debug("DELETE deleteModuleById lessonId deleted {}", lessonId);
        log.info("Lesson lessonId {} deleted!", lessonId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson deleted sucessfully");
    }

    @PutMapping("{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID modulesId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Validated LessonDto lessonDto) {

        log.debug("PUT updateLesson moduleId {} lessonId {} received", modulesId, lessonId);
        Optional<LessonModel> lesson = this.lessonService.findLessonIntoModule(modulesId, lessonId);
        if (!lesson.isPresent()) {
            log.warn("PUT updateLesson Lesson {} Not Found For This Module {}", lessonId, modulesId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found For This Module!");
        }

        LessonModel lessonModel = lesson.get();
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setTitle(lessonDto.getDescription());
        lessonModel.setTitle(lessonDto.getVideoUrl());

        log.debug("PUT updateLesson lessonId updated {}", lessonId);
        log.info("Lesson lessonId {} updated!", lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.save(lessonModel));
    }

    @GetMapping
    public ResponseEntity<Page<LessonModel>> findAllLessons(@PathVariable(value = "moduleId") UUID modulesId,
                                                            Specification<LessonModel> spec,
                                                            @PageableDefault(page = 0, size = 10, sort = "moduleId",
                                                                    direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.findAllLessonsIntoModule(
                SpecificationTemplate.lessonModuleIdSpec(modulesId).and(spec), pageable));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Object> findLessonById(@PathVariable(value = "moduleId") UUID modulesId,
                                                 @PathVariable(value = "lessonId") UUID lessonId) {

        log.debug("GET findLessonById ModuleId {} lessonId {} received", modulesId, lessonId);
        Optional<LessonModel> lesson = this.lessonService.findLessonIntoModule(modulesId, lessonId);
        if (!lesson.isPresent()) {
            log.warn("GET findLessonById Lesson {} Not Found For This Module {}!", lessonId ,modulesId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found For This Module!");
        }
        log.debug("GET findLessonById lessonId {}", lessonId);
        log.info("Lesson lessonId {} found!", lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(lesson.get());
    }
}
