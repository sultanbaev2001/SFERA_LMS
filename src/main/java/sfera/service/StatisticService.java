package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.enums.ERole;
import sfera.payload.ApiResponse;
import sfera.payload.StatisticDto;
import sfera.repository.CategoryRepository;
import sfera.repository.GroupRepository;
import sfera.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    public ApiResponse getAllCount(){
        Integer teacherCount = userRepository.countByRoleAndActiveTrue(ERole.ROLE_TEACHER);
        Integer studentCount = userRepository.countByRoleAndActiveTrue(ERole.ROLE_STUDENT);
        Integer categoryCount = categoryRepository.countByIsActiveTrue();
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
        return null;
    }
}
