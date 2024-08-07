package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqHomeWork;
import sfera.security.CurrentUser;
import sfera.service.StudentService;

import java.util.List;


@Tag(name = "Student Controller")
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Studenti infosi")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getStatisticStudent(@CurrentUser User user){
        ApiResponse apiResponse = studentService.getCountAllAndAvailableLessonsAndScoreAndRate(user.getId());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Studentlarni reytingi")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/rating")
    public ResponseEntity<ApiResponse> getRatingStudents(@CurrentUser User user){
        ApiResponse ratingStudents = studentService.getRatingStudents(user);
        return ResponseEntity.status(ratingStudents.getStatus()).body(ratingStudents);
    }

    @Operation(summary = "Studenti lessonlari")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/lessons")
    public ResponseEntity<ApiResponse> getAllLessons(@CurrentUser User user){
        ApiResponse api = studentService.getStudentLessons(user);
        return ResponseEntity.status(api.getStatus()).body(api);
    }

    @Operation(summary = "Studentni ustiga bosgandagi lessoni")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/lesson/{id}")
    public ResponseEntity<ApiResponse> getLesson(@PathVariable Integer id){
        ApiResponse api = studentService.getStudentLesson(id);
        return ResponseEntity.status(api.getStatus()).body(api);
    }

    @Operation(summary = "Student tasklarini korish")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse> getStudentTasks(@CurrentUser User user){
        ApiResponse apiResponse = studentService.getStudentTasks(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Student homeworkni jonatish")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/homework")
    public ResponseEntity<ApiResponse> addStudentHomeWork(@RequestBody List<ReqHomeWork> homeWorks,
                                                          @CurrentUser User user){
        ApiResponse apiResponse = studentService.addStudentHomeWork(homeWorks, user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
