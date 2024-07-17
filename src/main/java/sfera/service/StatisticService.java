package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Group;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.payload.ApiResponse;
import sfera.payload.StatisticDto;
import sfera.payload.res.ResCategory;
import sfera.payload.top.TopStudentDTO;
import sfera.repository.CategoryRepository;
import sfera.repository.GroupRepository;
import sfera.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final HomeWorkService homeWorkService;

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
        int sum=0, count = 0;
        List<Category> categories = categoryRepository.findByActiveTrue();
        List<ResCategory> resCategoryList= new ArrayList<>();
        if (categories.isEmpty()){
            return new ApiResponse("Categories not found",false,HttpStatus.BAD_REQUEST,null);
        }
        for (Category category : categories){
            double percentage=0;
            List<User> students = userRepository.findAllByGroup_CategoryAndRoleAndActiveTrue(category, ERole.ROLE_STUDENT);
            for (User student : students){
                double percentageOfMonth = homeWorkService.getHomeworksByStudentPercentageOfMonth(student);
                percentage+=percentageOfMonth;
            }
            ResCategory resCategory=ResCategory.builder()
                    .categoryName(category.getName())
                    .percentage(percentage*100)
                    .build();
            resCategoryList.add(resCategory);
        }
        return new ApiResponse("Success",true,HttpStatus.OK,resCategoryList);
    }















//    TOP Teacher, student and group service

//    Student
    public ApiResponse getTopStudent(){

        List<TopStudentDTO> studentList = new ArrayList<>();
        Map<UUID, Integer> topStudentMap = new HashMap<>();
        List<User> activeStudents = userRepository.findActiveRole(ERole.ROLE_STUDENT);
        if (!activeStudents.isEmpty()){
            for (User user : activeStudents) {
                Integer score = homeWorkService.getTotalScoreByStudentsAndCurrentMonth(user);
                TopStudentDTO topStudentDTO = TopStudentDTO.builder()
                        .studentId(user.getId())
                        .firstName(user.getFirstname())
                        .lastName(user.getLastname())
                        .groupName(user.getGroup().getName())
                        .ball(score)
                        .build();
                studentList.add(topStudentDTO);
                topStudentMap.put(user.getId(), score);
            }

            List<UUID> topStudentList = topStudentMap.entrySet().stream()
                    .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .toList();


            List<TopStudentDTO> topStudens = new ArrayList<>();
            for (UUID topStudent : topStudentList) {
                for (TopStudentDTO students : studentList) {
                    if (students.getStudentId()==topStudent){
                        topStudens.add(students);
                        break;
                    }
                }
            }
            return new ApiResponse("Success", true, HttpStatus.OK, topStudens);
        }
        return new ApiResponse("No active student found", false, HttpStatus.BAD_REQUEST, null);
    }


//    Group
    public ApiResponse getTopGroup(){
        for (Group group : groupRepository.findAllByActiveTrue()) {
            
        }


        return null;
    }


}
