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
import sfera.service.HomeWorkService;
import sfera.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final HomeWorkService homeWorkService;


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
    public ResponseEntity<ApiResponse> getTeacherSee(@PathVariable Long teacherId) {
        ApiResponse apiResponse = userService.getTeacher(teacherId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN teacherni activligini uzgartirish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/teacher-deActivate/{teacherId}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable Long teacherId,@RequestParam Boolean active){
        ApiResponse apiResponse = userService.deActiveTeacher(teacherId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "ADMIN Teacherni update qilish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("update-teacher/{teacherId}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable Long teacherId,@RequestBody ReqTeacher reqTeacher){
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping("/getAllStudents")
    public ResponseEntity<ApiResponse> getAllStudents(@CurrentUser User user) {
        ApiResponse apiResponse = userService.getAllStudents(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "TEACHER Studetni update qilish")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @PutMapping("/updateStudent/{studentId}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long studentId,@RequestBody ReqStudent studentDTO) {
        ApiResponse apiResponse = userService.updateStudent(studentId,studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @Operation(summary = "TEACHER Studentni delete qilish")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    @DeleteMapping("/deleteStudent/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long studentId) {
        ApiResponse apiResponse = userService.deleteStudent(studentId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Studentni activeni update qilish")
    @PutMapping("/updateActive/{studentId}")
    public ResponseEntity<ApiResponse> updateActiveInStudent(@PathVariable Long studentId, @RequestParam boolean active) {
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


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Homework student list")
    @GetMapping("/homework/list")
    public ResponseEntity<ApiResponse> getHomeworkList(@CurrentUser User user) {
        ApiResponse apiResponse = userService.getStudentList(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Homework student")
    @GetMapping("/homework")
    public ResponseEntity<ApiResponse> getHomework(@RequestParam UUID studentId, @RequestParam Integer lessonId) {
        ApiResponse apiResponse = userService.getStudentsHomework(studentId, lessonId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(summary = "TEACHER Homework ball qoyishi")
    @GetMapping("/homework/score")
    public ResponseEntity<ApiResponse> updateHomeworkScore(@RequestParam Long studentId, @RequestParam Integer homeworkId, @RequestParam Integer inScore) {
        ApiResponse apiResponse = homeWorkService.updateHomeworkScore(studentId, homeworkId, inScore);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


}
