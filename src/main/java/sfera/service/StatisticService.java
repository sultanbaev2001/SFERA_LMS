package sfera.service;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.HomeWork;
import sfera.entity.Lesson;
import sfera.entity.enums.ERole;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.StatisticDto;
import sfera.payload.res.ResCategory;
import sfera.repository.*;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;
    private final HomeWorkRepository homeWorkRepository;

    public ApiResponse getAllCount(){
        Integer teacherCount = userRepository.countByRoleAndActiveTrue(ERole.ROLE_TEACHER);
        Integer studentCount = userRepository.countByRoleAndActiveTrue(ERole.ROLE_STUDENT);
        Integer categoryCount = categoryRepository.countByActiveTrue();
        Integer groupCount = groupRepository.countByActiveTrue();
        StatisticDto statisticDto = StatisticDto.builder()
                .teacherCount(teacherCount)
                .studentCount(studentCount)
                .categoryCount(categoryCount)
                .groupCount(groupCount)
                .build();
        return new ApiResponse("Success",true, HttpStatus.OK,statisticDto);
    }
    public ApiResponse getPercentageByCategory(){
        List<Category> categories = categoryRepository.findByActiveTrue();
        Integer student = userRepository.countByRoleAndActiveTrue(ERole.ROLE_STUDENT);
        List<ResCategory> resCategoryList= new ArrayList<>();
        if (categories.isEmpty()){
            return new ApiResponse("Categories not found",false,HttpStatus.BAD_REQUEST,null);
        }
        for (Category category : categories){
            Integer studentCount = userRepository.countAllByGroup_CategoryAndRoleAndActiveTrue(category, ERole.ROLE_STUDENT);
            double percentage = (double) (studentCount * 100) / student;
            ResCategory resCategory=ResCategory.builder()
                    .categoryName(category.getName())
                    .percentage(percentage)
                    .build();
            resCategoryList.add(resCategory);
        }
        return new ApiResponse("Success",true,HttpStatus.OK,resCategoryList);
    }

}
