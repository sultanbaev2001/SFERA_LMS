package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqLesson;
import sfera.service.LessonService;


@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<ApiResponse> createLesson(@RequestBody ReqLesson reqLesson) {
        ApiResponse apiResponse = lessonService.saveLesson(reqLesson);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getLessons() {
        ApiResponse allLessons = lessonService.getAllLessons();
        return ResponseEntity.status(allLessons.getStatus()).body(allLessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<ApiResponse> getLesson(@PathVariable Integer lessonId) {
        ApiResponse oneLesson = lessonService.getOneLesson(lessonId);
        return ResponseEntity.status(oneLesson.getStatus()).body(oneLesson);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateLesson(@RequestBody ReqLesson reqLesson) {
        ApiResponse apiResponse = lessonService.updateLesson(reqLesson);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<ApiResponse> deleteLesson(@PathVariable Integer lessonId) {
        ApiResponse apiResponse = lessonService.deleteLesson(lessonId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/moduleByLessonCount/")
    public ResponseEntity<ApiResponse> getModuleByLessonCount(){
        ApiResponse apiResponse = lessonService.getModuleByLesson();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getModuleByLessons/{moduleId}")
    public ResponseEntity<ApiResponse> getModuleByLessons(@PathVariable Integer moduleId ) {
        ApiResponse apiResponse = lessonService.getLessons(moduleId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



}
