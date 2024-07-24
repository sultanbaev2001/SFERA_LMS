package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.security.CurrentUser;
import sfera.service.RateService;

@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @Operation(summary = "Admin rate panel uchun yillik guruhlar statistikasi")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/yearlyGroupBy/")
    public ResponseEntity<ApiResponse> getYearlyGroupBy() {
        ApiResponse apiResponse = rateService.getRateGroup();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin rate panel barcha studentlar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllStudent/")
    public ResponseEntity<ApiResponse> getAllStudent() {
        ApiResponse apiResponse = rateService.getAllStudentRate();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "Teacher dashboard uchun yillik statistika")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("teacher-dashboard/")
    public ResponseEntity<ApiResponse> getTeacherDashboard(@CurrentUser User user) {
        ApiResponse apiResponse = rateService.getStatisticForTeacher(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin rate paneldagi studentlar listi search,group, category bo'yicha")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("filter/")
    public ResponseEntity<ApiResponse> filter(@RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) Integer groupId,
                                              @RequestParam(required = false) Integer categoryId){
        ApiResponse students = rateService.getStudents(keyword, groupId, categoryId);
        return ResponseEntity.status(students.getStatus()).body(students);
    }



}
