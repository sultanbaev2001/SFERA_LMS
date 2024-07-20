package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.entity.Lesson;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqLesson;
import sfera.service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;



    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER  lesson qushish")
    @PostMapping
    public ResponseEntity<ApiResponse> createLesson(@RequestBody ReqLesson reqLesson) {
        ApiResponse apiResponse = lessonService.saveLesson(reqLesson);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER  barcha lessonlarni get qilish")
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
}
