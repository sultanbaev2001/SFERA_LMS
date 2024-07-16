package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqTeacher;
import sfera.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/teacherAdd")
    public ResponseEntity<ApiResponse> createUser(@RequestBody ReqTeacher reqTeacher) {
        ApiResponse apiResponse = userService.saveTeacher(reqTeacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/teacherList")
    public ResponseEntity<ApiResponse> getTeacherList() {
        ApiResponse apiResponse = userService.getAllTeachers();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/teacher-see/{teacherId}")
    public ResponseEntity<ApiResponse> getTeacherSee(@PathVariable UUID teacherId) {
        ApiResponse apiResponse = userService.getTeacher(teacherId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/teacher-deActivate/{teacherId}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable UUID teacherId,@RequestParam Boolean active){
        ApiResponse apiResponse = userService.deActiveTeacher(teacherId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("update-teacher/{teacherId}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable UUID teacherId,@RequestBody ReqTeacher reqTeacher){
        ApiResponse apiResponse = userService.editTeacher(teacherId, reqTeacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}
