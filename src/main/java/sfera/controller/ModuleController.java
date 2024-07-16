package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.ModuleDTO;
import sfera.service.ModuleService;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;
    @PostMapping
    public ResponseEntity<ApiResponse> saveModule(@RequestBody ModuleDTO moduleDTO){
        ApiResponse apiResponse = moduleService.saveModule(moduleDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getAllModules(){
        ApiResponse apiResponse = moduleService.getAllModules();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping("/{moduleId}")
    public ResponseEntity<ApiResponse> getModuleById(@PathVariable Integer moduleId){
        ApiResponse module = moduleService.getOneModule(moduleId);
        return ResponseEntity.status(module.getStatus()).body(module);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ApiResponse> updateModule(@PathVariable Integer moduleId, @RequestBody ModuleDTO moduleDTO){
        ApiResponse apiResponse = moduleService.updateModule(moduleId, moduleDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<ApiResponse> deleteModule(@PathVariable Integer moduleId){
        ApiResponse apiResponse = moduleService.deleteModule(moduleId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}