package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqTeacher;
import sfera.payload.req.ReqStudent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sfera.security.CurrentUser;
import sfera.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    
    @Operation(summary = "ADMIN teacher qushish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/teacherAdd")
    public ResponseEntity<ApiResponse> createUser(@RequestBody ReqTeacher reqTeacher) {
        ApiResponse apiResponse = userService.saveTeacher(reqTeacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN teacherlarni hammasini kurish")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/teacherList")
    public ResponseEntity<ApiResponse> getTeacherList() {
        ApiResponse apiResponse = userService.getAllTeachers();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN Bitta teacherni kurish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/teacher-see/{teacherId}")
    public ResponseEntity<ApiResponse> getTeacherSee(@PathVariable UUID teacherId) {
        ApiResponse apiResponse = userService.getTeacher(teacherId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN teacherni activligini uzgartirish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/teacher-deActivate/{teacherId}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable UUID teacherId,@RequestParam Boolean active){
        ApiResponse apiResponse = userService.deActiveTeacher(teacherId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN Teacherni update qilish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("update-teacher/{teacherId}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable UUID teacherId,@RequestBody ReqTeacher reqTeacher){
        ApiResponse apiResponse = userService.editTeacher(teacherId, reqTeacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN/TEACHER Studentni save qilish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @PostMapping("/saveStudent")
    public ResponseEntity<ApiResponse> saveStudent(@RequestBody ReqStudent studentDTO) {
        ApiResponse apiResponse = userService.saveStudent(studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "TEACHER Student panel get qilish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @GetMapping("/getAllStudents")
    public ResponseEntity<ApiResponse> getAllStudents(@CurrentUser User user) {
        ApiResponse apiResponse = userService.getAllStudents(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "TEACHER Studetni update qilish")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @PutMapping("/updateStudent/{studentId}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable UUID studentId,@RequestBody ReqStudent studentDTO) {
        ApiResponse apiResponse = userService.updateStudent(studentId,studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "TEACHER Studentni delete qilish")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @DeleteMapping("/deleteStudent/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable UUID studentId) {
        ApiResponse apiResponse = userService.deleteStudent(studentId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Studentni activeni update qilish")
    @PutMapping("/updateActive/{studentId}")
    public ResponseEntity<ApiResponse> updateActiveInStudent(@PathVariable UUID studentId, @RequestParam boolean active) {
        ApiResponse apiResponse = userService.updateActiveInStudent(studentId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Dashboard student list")
    @GetMapping("/teacher/studentList")
    public ResponseEntity<ApiResponse> getStudentByTeacher(@CurrentUser User user) {
        ApiResponse topStudentByTeacher = userService.getTopStudentByTeacher(user);
        return ResponseEntity.status(topStudentByTeacher.getStatus()).body(topStudentByTeacher);
    }
}
