package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.payload.ModuleDTO;
import sfera.security.CurrentUser;
import sfera.service.ModuleService;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_TEACHER')")
public class ModuleController {
    private final ModuleService moduleService;


    @Operation(summary = "TEACHER Modul save qilish")
    @PostMapping
    public ResponseEntity<ApiResponse> saveModule(@RequestBody ModuleDTO moduleDTO, @CurrentUser User teacher){
        ApiResponse apiResponse = moduleService.saveModule(moduleDTO, teacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "TEACHER Modul hammsini get qilish")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllModules(@CurrentUser User teacher){
        ApiResponse apiResponse = moduleService.getAllModules(teacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "TEACHER Modul bittasini get qilish")
    @GetMapping("/{moduleId}")
    public ResponseEntity<ApiResponse> getModuleById(@PathVariable Integer moduleId, @CurrentUser User teacher){
        ApiResponse module = moduleService.getOneModule(moduleId,teacher);
        return ResponseEntity.status(module.getStatus()).body(module);
    }

    @Operation(summary = "TEACHER Modul update qilish")
    @PutMapping("/{moduleId}")
    public ResponseEntity<ApiResponse> updateModule(@PathVariable Integer moduleId,
                                                    @RequestBody ModuleDTO moduleDTO,
                                                    @CurrentUser User teacher){
        ApiResponse apiResponse = moduleService.updateModule(moduleId, moduleDTO,teacher);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @Operation(summary = "TEACHER Modul delete qilish")
    @DeleteMapping("/{moduleId}")
    public ResponseEntity<ApiResponse> deleteModule(@PathVariable Integer moduleId){
        ApiResponse apiResponse = moduleService.deleteModule(moduleId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "TEACHER Categoryaga tegishli barcha modullarni get qilish")
    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<ApiResponse> getModuleByCategory(@PathVariable Integer categoryId){
        ApiResponse module = moduleService.getModuleByCategoryId(categoryId);
        return ResponseEntity.status(module.getStatus()).body(module);
    }

}
