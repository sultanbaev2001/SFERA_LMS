package sfera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.CategoryDTO;
import sfera.service.CategoryService;


@CrossOrigin
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public HttpEntity<ApiResponse> saveCategory(@RequestBody CategoryDTO categoryDTO){
        ApiResponse apiResponse = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping("/{categoryId}")
    public HttpEntity<ApiResponse> getOneCategory(@PathVariable Integer categoryId){
        ApiResponse apiResponse = categoryService.getOneCategory(categoryId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @GetMapping("/list")
    public HttpEntity<ApiResponse> getCategoryList(){
        ApiResponse allCategory = categoryService.getAllCategories();
        return ResponseEntity.status(allCategory.getStatus()).body(allCategory);
    }


    @PutMapping
    public HttpEntity<ApiResponse> updateCategory(@RequestBody CategoryDTO categoryDTO){
        ApiResponse apiResponse = categoryService.updateCategory(categoryDTO);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteCategory(@PathVariable Integer id){
        ApiResponse apiResponse = categoryService.deleteCategory(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


}
