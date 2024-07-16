package sfera.controller;


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
    @GetMapping("countBy/")
    public ResponseEntity<ApiResponse> getAllCountByStatistic(){
        ApiResponse apiResponse = statisticService.getAllCount();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("percentage/")
    public ResponseEntity<ApiResponse> getPercentageStatistic(){
        ApiResponse apiResponse = statisticService.getPercentageByCategory();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}
