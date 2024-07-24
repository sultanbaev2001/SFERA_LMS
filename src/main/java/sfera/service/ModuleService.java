package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Module;
import sfera.entity.User;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.ModuleDTO;
import sfera.repository.CategoryRepository;
import sfera.repository.ModuleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final CategoryRepository categoryRepository;

    public ApiResponse saveModule(ModuleDTO moduleDTO, User teacher) {
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
                    .teacher(teacher)
                    .build();
            moduleRepository.save(module);
        }
        return new ApiResponse("Module saved", HttpStatus.CREATED);
    }

    public ApiResponse getAllModules(User teacher){
        List<Module> modules = moduleRepository.findAll();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (Module module : modules) {
            ModuleDTO moduleDTO=ModuleDTO.builder()
                    .orderName(module.getOrderName())
                    .moduleId(module.getId())
                    .categoryId(module.getCategory().getId())
                    .teacherId(teacher.getId())
                    .build();
            moduleDTOs.add(moduleDTO);
        }
        return new ApiResponse("Modules", HttpStatus.OK, moduleDTOs);
    }


    public ApiResponse getOneModule(Integer id, User teacher){
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
        ModuleDTO moduleDTO= ModuleDTO.builder()
                .moduleId(module.getId())
                .orderName(module.getOrderName())
                .categoryId(module.getCategory().getId())
                .teacherId(teacher.getId())
                .build();
        return new ApiResponse("Success",HttpStatus.OK,moduleDTO);
    }

    public ApiResponse updateModule(Integer id,ModuleDTO moduleDTO, User teacher){
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
        Category category = categoryRepository.findById(moduleDTO.getCategoryId())
                .orElseThrow(() -> GenericException.builder().message("Category not found").statusCode(404).build());
        boolean existsed = moduleRepository.existsByOrderName(moduleDTO.getOrderName());
        if (!existsed){
            module.setId(id);
            module.setOrderName(moduleDTO.getOrderName());
            module.setCategory(category);
            module.setTeacher(teacher);
            moduleRepository.save(module);
            return new ApiResponse("Module successfully updated",HttpStatus.OK);
        }
        return new ApiResponse("Module already exists",HttpStatus.ALREADY_REPORTED);
    }

    public ApiResponse deleteModule(Integer id){
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
        moduleRepository.delete(module);
        return new ApiResponse("Module successfully deleted",HttpStatus.OK);
    }


    public ApiResponse getModuleByCategoryId(Integer id){
        List<Module> allByCategoryId = moduleRepository.findAllByCategory_Id(id);
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (Module module : allByCategoryId) {
            ModuleDTO moduleDTO= ModuleDTO.builder()
                    .moduleId(module.getId())
                    .orderName(module.getOrderName())
                    .categoryId(module.getCategory().getId())
                    .teacherId(module.getTeacher().getId())
                    .build();
            moduleDTOs.add(moduleDTO);
        }
        return new ApiResponse("Success",HttpStatus.OK,moduleDTOs);
    }
}
