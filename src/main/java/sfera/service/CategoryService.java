package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.exception.GenericException;
import sfera.mapper.CategoryMapper;
import sfera.payload.ApiResponse;
import sfera.payload.CategoryDTO;
import sfera.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    public ApiResponse addCategory(CategoryDTO categoryDTO) {
        boolean exists = categoryRepository.existsByName(categoryDTO.getName());
        if (!exists){
            Category category = categoryMapper.toEntity(categoryDTO);
            categoryRepository.save(category);
            return new ApiResponse("Success", true, HttpStatus.OK, null);
        }
        return new ApiResponse("Category already exsist", false, HttpStatus.BAD_REQUEST, null);
    }


    public ApiResponse getOneCategory(Integer id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> GenericException.builder()
                        .message("Category not found")
                        .statusCode(404)
                        .build());
        CategoryDTO categoryDTO = categoryMapper.toDTO(category);
        return new ApiResponse("Success", true, HttpStatus.OK, categoryDTO);
    }


    public ApiResponse getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOList.add(categoryMapper.toDTO(category));
        }
        return new ApiResponse("Success", true, HttpStatus.OK, categoryDTOList);
    }


    public ApiResponse updateCategory(CategoryDTO categoryDTO) {
        boolean exists = categoryRepository.existsByName(categoryDTO.getName());
        if (!exists){
            Category category = categoryMapper.toEntity(categoryDTO);
            categoryRepository.save(category);
            return new ApiResponse("Success", true, HttpStatus.OK, null);
        }
        return new ApiResponse("Category already exist", false, HttpStatus.BAD_REQUEST, null);
    }

    public ApiResponse deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
        return new ApiResponse("Success", true, HttpStatus.OK, null);
    }
}
