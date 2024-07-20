package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.service.RateService;

@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/yearlyGroupBy/")
    public ResponseEntity<ApiResponse> getYearlyGroupBy() {
        ApiResponse apiResponse = rateService.getRateGroup();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllStudent/")
    public ResponseEntity<ApiResponse> getAllStudent() {
        ApiResponse apiResponse = rateService.getAllStudentRate();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search-student/")
    public ResponseEntity<ApiResponse> getSearchStudents(@RequestParam String keyword) {
        ApiResponse apiResponse = rateService.searchStudentRate(keyword);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/groupByStudentRate/{groupId}")
    public ResponseEntity<ApiResponse> getGroupByStudentRate(@PathVariable int groupId) {
        ApiResponse apiResponse = rateService.getStudentRateByGroup(groupId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/categoryByStudentRate/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryByStudentRate(@PathVariable int categoryId) {
        ApiResponse apiResponse = rateService.getStudentRateByCategory(categoryId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }



}
