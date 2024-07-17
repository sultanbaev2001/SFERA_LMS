package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.security.CurrentUser;
import sfera.service.StudentService;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getStatisticStudent(@CurrentUser User user){
        ApiResponse apiResponse = studentService.getCountAllAndAvailableLessonsAndScoreAndRate(user.getId());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
