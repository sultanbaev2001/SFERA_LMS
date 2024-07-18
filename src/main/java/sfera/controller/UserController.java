package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqTeacher;
import sfera.payload.req.ReqStudent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @PostMapping("/saveStudent")
    public ResponseEntity<ApiResponse> saveStudent(@RequestBody ReqStudent studentDTO) {
        ApiResponse apiResponse = userService.saveStudent(studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @GetMapping("/getAllStudents")
    public ResponseEntity<ApiResponse> getAllStudents() {
        ApiResponse apiResponse = userService.getAllStudents();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @PutMapping("/updateStudent/{studentId}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable UUID studentId,@RequestBody ReqStudent studentDTO) {
        ApiResponse apiResponse = userService.updateStudent(studentId,studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @DeleteMapping("/deleteStudent/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable UUID studentId) {
        ApiResponse apiResponse = userService.deleteStudent(studentId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @PutMapping("/updateActive/{studentId}")
    public ResponseEntity<ApiResponse> updateActiveInStudent(@PathVariable UUID studentId, @RequestParam boolean active) {
        ApiResponse apiResponse = userService.updateActiveInStudent(studentId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
