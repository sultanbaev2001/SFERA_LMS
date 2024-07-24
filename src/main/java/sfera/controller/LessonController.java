package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqLesson;
import sfera.security.CurrentUser;
import sfera.service.LessonService;


@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;



    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER  lesson qushish")
    @PostMapping
    public ResponseEntity<ApiResponse> createLesson(@RequestBody ReqLesson reqLesson, @CurrentUser User teacher) {
        ApiResponse apiResponse = lessonService.saveLesson(reqLesson,teacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN paneldagi lessonlar uchun")
    @GetMapping
    public ResponseEntity<ApiResponse> getLessons() {
        ApiResponse allLessons = lessonService.getAllLessons();
        return ResponseEntity.status(allLessons.getStatus()).body(allLessons);
    }


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER  Bitta lessonni get qilish")
    @GetMapping("/{lessonId}")
    public ResponseEntity<ApiResponse> getLesson(@PathVariable Integer lessonId) {
        ApiResponse oneLesson = lessonService.getOneLesson(lessonId);
        return ResponseEntity.status(oneLesson.getStatus()).body(oneLesson);
    }



    @Operation(summary = "TEACHER panelidagi lesson uchun")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/byTeacher")
    public ResponseEntity<ApiResponse> getLessonsByTeacher(@CurrentUser User teacher) {
        ApiResponse lesson = lessonService.getAllLessonByTeacher(teacher);
        return ResponseEntity.status(lesson.getStatus()).body(lesson);
    }


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Lessonni  update qilish")
    @PutMapping
    public ResponseEntity<ApiResponse> updateLesson(@RequestBody ReqLesson reqLesson) {
        ApiResponse apiResponse = lessonService.updateLesson(reqLesson);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER lessonni delete qilish")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<ApiResponse> deleteLesson(@PathVariable Integer lessonId) {
        ApiResponse apiResponse = lessonService.deleteLesson(lessonId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin lessonlarni royxatini olish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/moduleByLessonCount/")
    public ResponseEntity<ApiResponse> getModuleByLessonCount(){
        ApiResponse apiResponse = lessonService.getModuleByLesson();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin module boyicha lessonlarni korish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getModuleByLessons/{moduleId}")
    public ResponseEntity<ApiResponse> getModuleByLessons(@PathVariable Integer moduleId ) {
        ApiResponse apiResponse = lessonService.getLessons(moduleId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



}
