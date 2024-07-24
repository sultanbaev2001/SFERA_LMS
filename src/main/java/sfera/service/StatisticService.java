package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Group;
import sfera.entity.HomeWork;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.payload.ApiResponse;
import sfera.payload.StatisticDto;
import sfera.payload.res.CategoryStatistics;
import sfera.payload.res.ResCategory;
import sfera.payload.top.TopGroupDTO;
import sfera.payload.top.TopStudentDTO;
import sfera.payload.top.TopTeacherDTO;
import sfera.repository.CategoryRepository;
import sfera.repository.GroupRepository;
import sfera.repository.HomeWorkRepository;
import sfera.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final HomeWorkService homeWorkService;
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
                    .percentage(percentage)
                    .build();
            resCategoryList.add(resCategory);
        }
        return new ApiResponse("Success",true,HttpStatus.OK,resCategoryList);
    }


    public ApiResponse getCategoryByYearlyStatistic(){
        List<CategoryStatistics> categoryStatistics = homeWorkRepository.findCategoryStatistics();
        return new ApiResponse("Success",true,HttpStatus.OK,categoryStatistics);
    }
















//    TOP Teacher, student and group service

//    Student
    public ApiResponse getTopStudent(){

        List<User> activeStudents = userRepository.findByRole(ERole.ROLE_STUDENT);
        if (!activeStudents.isEmpty()){
            Map<TopStudentDTO, Integer> topStudentMap = new HashMap<>();
            for (User user : activeStudents) {
                if (user.isActive()){
                    Integer score = homeWorkService.getTotalScoreByStudentsAndCurrentMonth(user);
                    if(score == null){
                        continue;
                    }
                    TopStudentDTO topStudentDTO = TopStudentDTO.builder()
                            .studentId(user.getId())
                            .fullName(user.getFirstname() + " " + user.getLastname())
                            .groupName(user.getGroup().getName())
                            .score(score)
                            .build();
                    topStudentMap.put(topStudentDTO, score);
                }
            }


                List<TopStudentDTO> topStudens = topStudentMap.entrySet().stream()
                        .sorted(Map.Entry.<TopStudentDTO, Integer>comparingByValue().reversed())
                        .limit(5)
                        .map(Map.Entry::getKey)
                        .toList();

            return new ApiResponse("Success", true, HttpStatus.OK, topStudens);
        }
        return new ApiResponse("Failed", false, HttpStatus.BAD_REQUEST, null);
    }


//    Group

    public ApiResponse getTopGroup(){
        Map<TopGroupDTO, Integer> topGroupMap = new HashMap<>();
        List<Group> groups = groupRepository.findAllByActiveTrue();
        if (!groups.isEmpty()){
            for (Group group : groups) {
                Integer score = homeWorkService.getTotalScoreByGroupAndCurrentMonth(group);
                TopGroupDTO topGroupDTO = TopGroupDTO.builder()
                        .groupId(group.getId())
                        .groupName(group.getName())
                        .score(score)
                        .build();
                topGroupMap.put(topGroupDTO, score);
            }

            List<TopGroupDTO> topGroups = topGroupMap.entrySet().stream()
                    .sorted(Map.Entry.<TopGroupDTO, Integer>comparingByValue().reversed())
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .toList();

            return new ApiResponse("Success", true, HttpStatus.OK, topGroups);
        }
        return new ApiResponse("Failed", false, HttpStatus.BAD_REQUEST, null);
    }


//    Teacher

    public ApiResponse getTopTeacher() {
        List<User> teachers = userRepository.findByRole(ERole.ROLE_TEACHER);

        if (!teachers.isEmpty()) {
            List<TopTeacherDTO> list = new ArrayList<>();
            for (User teacher : teachers) {
                if (teacher.isActive()) {
                    int sumScore = 0;
                    List<Group> groups = groupRepository.findAllByTeacherId(teacher.getId());
                    sumScore = groups.stream().filter(Group::isActive)
                            .map(homeWorkService::getTotalScoreByGroupAndCurrentMonth)
                            .filter(Objects::nonNull)
                            .mapToInt(Integer::intValue).sum();

                    TopTeacherDTO topTeacherDTO = TopTeacherDTO.builder()
                            .teacherId(teacher.getId())
                            .fullName(teacher.getFirstname() + " " + teacher.getLastname())
                            .phoneNumber(teacher.getPhoneNumber())
                            .score(sumScore)
                            .build();

                    list.add(topTeacherDTO);
                }
            }

            List<TopTeacherDTO> list1 = list.stream().sorted(Comparator.comparing(TopTeacherDTO::getScore)
                    .reversed()).limit(5).toList();

            return new ApiResponse("Success", true, HttpStatus.OK, list1);
        }
        return new ApiResponse("Failed", false, HttpStatus.BAD_REQUEST, null);
    }

}
