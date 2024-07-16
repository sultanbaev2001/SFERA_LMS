package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Module;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.ModuleDTO;
import sfera.repository.CategoryRepository;
import sfera.repository.ModuleRepository;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final CategoryRepository categoryRepository;

    public ApiResponse saveModule(ModuleDTO moduleDTO) {
        boolean existsed = moduleRepository.existsByOrderName(moduleDTO.getOrderName());
        Category category = categoryRepository.findById(moduleDTO.getCategoryId())
                .orElseThrow(() -> GenericException.builder()
                        .message("Category not found")
                        .statusCode(404)
                        .build());
        if (!existsed) {
            Module module= Module.builder()
                    .orderName(moduleDTO.getOrderName())
                    .category(category)
                    .build();
            moduleRepository.save(module);
        }
        return new ApiResponse("Module saved", HttpStatus.CREATED);
    }
}
