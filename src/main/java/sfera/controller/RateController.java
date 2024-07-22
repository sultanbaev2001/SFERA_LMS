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

    @Operation(summary ="Admin rate panel studentni search qilish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search-student/")
    public ResponseEntity<ApiResponse> getSearchStudents(@RequestParam String keyword) {
        ApiResponse apiResponse = rateService.searchStudentRate(keyword);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin rate panel group boyicha studentlar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/groupByStudentRate/{groupId}")
    public ResponseEntity<ApiResponse> getGroupByStudentRate(@PathVariable int groupId) {
        ApiResponse apiResponse = rateService.getStudentRateByGroup(groupId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin rate panel category boyicha studentlar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/categoryByStudentRate/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryByStudentRate(@PathVariable int categoryId) {
        ApiResponse apiResponse = rateService.getStudentRateByCategory(categoryId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Teacher dashboard uchun yillik statistika")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("teacher-dashboard/")
    public ResponseEntity<ApiResponse> getTeacherDashboard(@CurrentUser User user) {
        ApiResponse apiResponse = rateService.getStatisticForTeacher(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



}
