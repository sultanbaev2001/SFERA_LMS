package sfera.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sfera.payload.ApiResponse;
import sfera.service.StatisticService;

@RestController
@RequestMapping("statistic/")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN category, modul, teacher va studentlarni sonini olish")
    @GetMapping("countBy/")
    public ResponseEntity<ApiResponse> getAllCountByStatistic(){
        ApiResponse apiResponse = statisticService.getAllCount();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "ADMIN bir oylik statistika",description = "ADMIN dashboard dumaloq statistika")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("percentage/")
    public ResponseEntity<ApiResponse> getPercentageStatistic(){
        ApiResponse apiResponse = statisticService.getPercentageByCategory();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin top 5ta studentni korishi uchun")
    @GetMapping("top/student")
    public ResponseEntity<ApiResponse> getTopStudent(){
        ApiResponse apiResponse = statisticService.getTopStudent();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "Admin top 5ta guruhni korishi uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("top/group")
    public ResponseEntity<ApiResponse> getTopGroup(){
        ApiResponse apiResponse = statisticService.getTopGroup();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "Admin top 5ta teacherni korishi uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("top/teacher")
    public ResponseEntity<ApiResponse> getTopTeacher(){
        ApiResponse apiResponse = statisticService.getTopTeacher();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
