package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.StudentDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sfera.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse> saveStudent(StudentDTO studentDTO) {
        ApiResponse apiResponse = userService.saveStudent(studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getAllStudents() {
        ApiResponse apiResponse = userService.getAllStudents();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PutMapping
    public ResponseEntity<ApiResponse> updateStudent(StudentDTO studentDTO) {
        ApiResponse apiResponse = userService.updateStudent(studentDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable UUID studentId) {
        ApiResponse apiResponse = userService.deleteStudent(studentId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
