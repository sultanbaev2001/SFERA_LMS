package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Group;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.res.GroupStatistics;
import sfera.payload.res.ResRateStudent;
import sfera.repository.CategoryRepository;
import sfera.repository.GroupRepository;
import sfera.repository.HomeWorkRepository;
import sfera.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RateService {


    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final HomeWorkService homeWorkService;
    private final HomeWorkRepository homeWorkRepository;
    private final CategoryRepository categoryRepository;

    public ApiResponse getStudentRateByGroup(int groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> GenericException.builder()
                .message("Group not found").statusCode(400).build());
        List<User> students = userRepository.findAllByGroupAndRoleAndActiveTrue(group, ERole.ROLE_STUDENT);
        List<ResRateStudent> rateStudents = getStudentRateList(students);
        return new ApiResponse("Success", HttpStatus.OK, rateStudents);
    }

    public ApiResponse searchStudentRate(String keyword) {
        List<User> students = userRepository.searchByText(keyword);
        if (students.isEmpty()) {
            return new ApiResponse("User not found", HttpStatus.NOT_FOUND);
        }
        List<ResRateStudent> studentRateList = getStudentRateList(students);
        return new ApiResponse("Success", HttpStatus.OK, studentRateList);
    }

    public ApiResponse getAllStudentRate() {
        List<User> students = userRepository.findByRole(ERole.ROLE_STUDENT);
        List<ResRateStudent> studentRateList = getStudentRateList(students);
        return new ApiResponse("Success", HttpStatus.OK, studentRateList);
    }

    public ApiResponse getStudentRateByCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> GenericException.builder()
                .message("Category not found").statusCode(400).build());
        List<User> students = userRepository.findAllByGroup_CategoryAndRoleAndActiveTrue(category, ERole.ROLE_STUDENT);
        List<ResRateStudent> studentRateList = getStudentRateList(students);
        return new ApiResponse("Success", HttpStatus.OK, studentRateList);
    }

    public ApiResponse getRateGroup(){
        List<GroupStatistics> groupStatistics = homeWorkRepository.findGroupStatistics();
        return new ApiResponse("Success", HttpStatus.OK, groupStatistics);
    }




    private List<ResRateStudent> getStudentRateList(List<User> students) {
        Map<ResRateStudent, Integer> rateStudentMap = new HashMap<>();
        for (User user : students) {
            Integer score = homeWorkService.getTotalScoreByStudentsAndCurrentMonth(user);
            Integer ratingStudent = homeWorkRepository.getRatingStudent(user.getGroup().getId(), user.getId());
            ResRateStudent resRateStudent = ResRateStudent.builder()
                    .fullName(user.getFirstname() + " " + user.getLastname())
                    .categoryName(user.getGroup().getCategory().getName())
                    .groupName(user.getGroup().getName())
                    .rate(ratingStudent)
                    .score(score).build();
            rateStudentMap.put(resRateStudent, score);
        }
        return rateStudentMap.entrySet().stream()
                .sorted(Map.Entry.<ResRateStudent, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
    }



}
