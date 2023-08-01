package com.ead.course.controller;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
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

        if (!this.moduleService.existsById(modulesId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found!");
        }

        ModuleModel moduleModel = this.moduleService.findModuleById(modulesId).get();

        LessonModel lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.lessonService.save(lessonModel));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Object> deleteModuleById(@PathVariable(value = "moduleId") UUID moduleId,
                                                   @PathVariable(value = "lessonId") UUID lessonId) {

        Optional<ModuleModel> module = this.moduleService.findModuleById(moduleId);
        if (!module.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found!");
        }
        Optional<LessonModel> lesson = this.lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!lesson.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found For This Module!");
        }
        this.lessonService.deleteLesson(lesson.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson deleted sucessfully");
    }

    @PutMapping("{lessonId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "moduleId") UUID modulesId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Validated LessonDto lessonDto) {

        Optional<LessonModel> lesson = this.lessonService.findLessonIntoModule(modulesId, lessonId);
        if (!lesson.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found For This Module!");
        }

        LessonModel lessonModel = lesson.get();
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setTitle(lessonDto.getDescription());
        lessonModel.setTitle(lessonDto.getVideoUrl());

        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.save(lessonModel));
    }

    @GetMapping
    public ResponseEntity<List<LessonModel>> findAllLessons(@PathVariable(value = "moduleId") UUID modulesId) {

        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.findAllLessonsIntoModule(modulesId));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Object> findModuleById(@PathVariable(value = "moduleId") UUID modulesId,
                                                 @PathVariable(value = "lessonId") UUID lessonId) {

        Optional<LessonModel> lesson = this.lessonService.findLessonIntoModule(modulesId, lessonId);
        if (!lesson.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found For This Module!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(lesson.get());
    }
}
