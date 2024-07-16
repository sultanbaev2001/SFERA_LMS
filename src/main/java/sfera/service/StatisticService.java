package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Feedback;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.payload.ApiResponse;
import sfera.payload.StatisticDto;
import sfera.payload.res.ResCategory;
import sfera.payload.top.TopTeacherDTO;
import sfera.repository.CategoryRepository;
import sfera.repository.FeedbackRepository;
import sfera.repository.GroupRepository;
import sfera.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final FeedbackRepository feedbackRepository;

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













//    TOP Teacher, student and group service

//    Teacher
//    public ApiResponse getTopTeacher(){
//        Map<UUID, Double> topTeacherMap = new HashMap<>();
//        List<TopTeacherDTO> teacherList = new ArrayList<>();
//        List<User> activeTeachers = userRepository.findActiveTeachers();
//        if (!activeTeachers.isEmpty()){
//            for (User activeTeacher : activeTeachers) {
//                double sum=0;
//                int count = feedbackRepository.countByTeacherId(activeTeacher.getId());
//                for (Feedback feedback : feedbackRepository.findByTeacherId(activeTeacher.getId())) {
//                    sum+=feedback.getRate();
//                }
//                TopTeacherDTO topTeacherDTO = TopTeacherDTO.builder()
//                        .teacherId(activeTeacher.getId())
//                        .firstName(activeTeacher.getFirstname())
//                        .lastName(activeTeacher.getLastname())
//                        .phoneNumber(activeTeacher.getPhoneNumber())
//                        .rating(sum/count)
//                        .build();
//                topTeacherMap.put(topTeacherDTO.getTeacherId(), topTeacherDTO.getRating());
//                teacherList.add(topTeacherDTO);
//            }
//
//            List<UUID> topTeacherList = topTeacherMap.entrySet().stream()
//                    .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
//                    .limit(5)
//                    .map(Map.Entry::getKey)
//                    .toList();
//
//            List<TopTeacherDTO> topTeachers = new ArrayList<>();
//            for (UUID topTeacher : topTeacherList) {
//                for (TopTeacherDTO teachers : teacherList) {
//                    if (teachers.getTeacherId()==topTeacher){
//                        topTeachers.add(teachers);
//                        break;
//                    }
//                }
//            }
//            return new ApiResponse("Success", true, HttpStatus.OK, topTeachers);
//        }
//        return new ApiResponse("No active teachers found", false, HttpStatus.BAD_REQUEST, null);
//    }



//    Student


}
