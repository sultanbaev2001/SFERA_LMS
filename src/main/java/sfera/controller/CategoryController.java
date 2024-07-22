package sfera.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.CategoryDTO;
import sfera.service.CategoryService;

import javax.swing.plaf.SeparatorUI;


@CrossOrigin
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @Operation(summary = "ADMIN  Category save qilish")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<ApiResponse> saveCategory(@RequestBody CategoryDTO categoryDTO){
        ApiResponse apiResponse = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Category bittasini get qilish")
    @GetMapping("/{categoryId}")
    public HttpEntity<ApiResponse> getOneCategory(@PathVariable Integer categoryId){
        ApiResponse apiResponse = categoryService.getOneCategory(categoryId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary ="ADMIN   Category hammasini  kurish")
    @GetMapping("/list")
    public HttpEntity<ApiResponse> getCategoryList(){
        ApiResponse allCategory = categoryService.getAllCategories();
        return ResponseEntity.status(allCategory.getStatus()).body(allCategory);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Categoryni update qilish")
    @PutMapping
    public HttpEntity<ApiResponse> updateCategory(@RequestBody CategoryDTO categoryDTO){
        ApiResponse apiResponse = categoryService.updateCategory(categoryDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Categoryni delete qilish")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteCategory(@PathVariable Integer id){
        ApiResponse apiResponse = categoryService.deleteCategory(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


}
