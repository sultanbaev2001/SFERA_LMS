package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.payload.CategoryDTO;
import sfera.payload.req.ReqLessonTracking;
import sfera.security.CurrentUser;
import sfera.service.LessonTrackingService;

@RestController
@RequestMapping("/lesson/tracking")
@RequiredArgsConstructor
public class LessonTrackingController {

    private final LessonTrackingService lessonTrackingService;


    @Operation(summary = "Teacher lessonga o'ziga tegishli guruhni biriktirishni saqlashi")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping
    public HttpEntity<ApiResponse> saveLessonTracking(@RequestBody ReqLessonTracking reqLessonTracking, @CurrentUser User user){
        ApiResponse apiResponse = lessonTrackingService.saveLessonTracking(reqLessonTracking, user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Teacher lessonga o'ziga tegishli guruhni biriktirishni o'zgartirishi")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PutMapping("/update/{id}")
    public HttpEntity<ApiResponse> updateLessonTracking(@PathVariable("id") Integer id, @RequestBody ReqLessonTracking reqLessonTracking, @CurrentUser User user){
        ApiResponse apiResponse = lessonTrackingService.updateLessonTracking(id, reqLessonTracking, user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "Teacher lessonga guruhni biriktirishni o'ziga tegishli bittasini ko'rishi")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("{id}")
    public HttpEntity<ApiResponse> getLessonTracking(@PathVariable("id") Integer id, @CurrentUser User user){
        ApiResponse apiResponse = lessonTrackingService.getLessonTracking(id, user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "Teacher lessonga guruhni biriktirishni o'ziga tegishli hammasini ko'rishi")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping
    public HttpEntity<ApiResponse> getLessonTrackingList(@CurrentUser User user){
        ApiResponse apiResponse = lessonTrackingService.getTeacherByLessonTrackingList(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "Teacher lessonga guruhni biriktirishni o'chirishi")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteLessonTracking(@PathVariable("id") Integer id){
        ApiResponse apiResponse = lessonTrackingService.deleteLessonTracking(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }




}
