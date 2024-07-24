package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.entity.Group;
import sfera.entity.Lesson;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.exception.UserNotFoundException;
import sfera.payload.ApiResponse;
import sfera.payload.res.ResStudentDTO;
import sfera.payload.teacher_homework.StudentHomeworkDTO;
import sfera.payload.TeacherDto;
import sfera.payload.req.ReqTeacher;
import sfera.payload.res.ResGroupStudentCount;
import sfera.payload.res.ResStudent;
import sfera.payload.res.ResTeacher;
import sfera.payload.teacher_homework.StudentListDto;
import sfera.repository.GroupRepository;
import sfera.exception.GenericException;
import sfera.payload.req.ReqStudent;
import sfera.repository.HomeWorkRepository;
import sfera.repository.LessonRepository;
import sfera.repository.UserRepository;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final HomeWorkRepository homeWorkRepository;
    private final HomeWorkService homeWorkService;
    private final LessonRepository lessonRepository;

    public ApiResponse saveTeacher(ReqTeacher reqTeacher){
        boolean exists = userRepository.existsByPhoneNumber(reqTeacher.getPhoneNumber());
        if(exists){
            return new ApiResponse("Phone number already exits",false, HttpStatus.BAD_REQUEST,null);
        }
        User user=User.builder()
                .firstname(reqTeacher.getFirstName())
                .lastname(reqTeacher.getLastName())
                .phoneNumber(reqTeacher.getPhoneNumber())
                .password(passwordEncoder.encode(reqTeacher.getPassword()))
                .role(ERole.ROLE_TEACHER)
                .active(true)
                .build();
        userRepository.save(user);
        return new ApiResponse("Successfully saved teacher",true, HttpStatus.OK,null);
    }

    public ApiResponse getAllTeachers(){
        List<User> users = userRepository.findByRole(ERole.ROLE_TEACHER);
        List<ResTeacher> resTeachers = new ArrayList<>();
        if (users.isEmpty()){
            return new ApiResponse("No users found",false, HttpStatus.BAD_REQUEST,null);
        }
        for (User user : users){
            ResTeacher resTeacher = ResTeacher.builder()
                    .teacherID(user.getId())
                    .firstName(user.getFirstname())
                    .lastName(user.getLastname())
                    .phoneNumber(user.getPhoneNumber())
                    .active(user.isActive())
                    .build();
            resTeachers.add(resTeacher);
        }
        return new ApiResponse("Successfully retrieved users",true, HttpStatus.OK,resTeachers);
    }

    public ApiResponse getTeacher(UUID teacherId){
        User user = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        List<Group> groups = groupRepository.findAllByTeacherId(user.getId());
        List<ResGroupStudentCount> resGroupStudentCounts=new ArrayList<>();
        Set<String> categoryName=new HashSet<>();
        for (Group group : groups){
            Integer studentCount = userRepository.countAllByGroupAndRole(group, ERole.ROLE_STUDENT);
            categoryName.add(group.getCategory().getName());
            ResGroupStudentCount resGroupStu=ResGroupStudentCount.builder()
                    .groupName(group.getName())
                    .studentCount(studentCount)
                    .build();
            resGroupStudentCounts.add(resGroupStu);
        }
        TeacherDto teacherDto = TeacherDto.builder()
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .active(user.isActive())
                .resGroupStudentCount(resGroupStudentCounts)
                .categoryName(categoryName)
                .build();
        return new ApiResponse("Success",true, HttpStatus.OK,teacherDto);
    }

    public  ApiResponse deActiveTeacher(UUID teacherId, Boolean active){
        User user = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        user.setActive(active);
        userRepository.save(user);
        return new ApiResponse("Successfully edited user",true, HttpStatus.OK,null);
    }

    public ApiResponse editTeacher(UUID teacherId, ReqTeacher reqTeacher){
        User user = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        user.setFirstname(reqTeacher.getFirstName());
        user.setLastname(reqTeacher.getLastName());
        user.setPhoneNumber(reqTeacher.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(reqTeacher.getPassword()));
        userRepository.save(user);
        return new ApiResponse("Successfully edited user",true, HttpStatus.OK,null);
    }


    public ApiResponse saveStudent(ReqStudent studentDTO) {
        Group group = groupRepository.findById(studentDTO.getGroupId())
                .orElseThrow(() -> GenericException.builder().message("Group not found").statusCode(404).build());
        boolean existsed = userRepository.existsByPhoneNumber(studentDTO.getPhoneNumber());
        if(!existsed){
            User user = User.builder()
                    .firstname(studentDTO.getFirstname())
                    .lastname(studentDTO.getLastname())
                    .phoneNumber(studentDTO.getPhoneNumber())
                    .password(passwordEncoder.encode(studentDTO.getPassword()))
                    .group(group)
                    .role(ERole.ROLE_STUDENT)
                    .active(true)
                    .build();
            userRepository.save(user);
            return new ApiResponse("Student successfully saved", HttpStatus.OK);
        }
        return new ApiResponse("Student already exists",true, HttpStatus.BAD_REQUEST,null);
    }

    public ApiResponse getAllStudentsByTeacher(User teacher) {
        List<ResStudent> resStudentList = new ArrayList<>();
        List<Group> allByTeacherId = groupRepository.findAllByTeacherId(teacher.getId());
        for (Group group : allByTeacherId) {
            List<User> users = userRepository.findAllByGroupId(group.getId());
            for (User user : users) {
                if (user.getRole().equals(ERole.ROLE_STUDENT)) {
                    int allScoreByStudent = homeWorkRepository.findAllScoreByStudent(user.getId());
                    Integer ratingStudent = homeWorkRepository.getRatingStudent(group.getId(), user.getId());
                    ResStudent resStudent = ResStudent.builder()
                            .fullName(user.getFirstname() + " " + user.getLastname())
                            .phoneNumber(user.getPhoneNumber())
                            .categoryName(user.getGroup().getCategory().getName())
                            .groupName(group.getName())
                            .ball(allScoreByStudent)
                            .rate(ratingStudent)
                            .active(user.isActive())
                            .build();
                    resStudentList.add(resStudent);
                }
            }
        }
        return new ApiResponse("All students successfully retrieved", HttpStatus.OK, resStudentList);
    }



    public ApiResponse getAllStudents(){
        List<User> byRole = userRepository.findByRole(ERole.ROLE_STUDENT);
        List<ResStudentDTO> resStudentDTOList = new ArrayList<>();
        for (User user : byRole) {
            ResStudentDTO resStudentDTO= ResStudentDTO.builder()
                    .fullName(user.getFirstname() + " " + user.getLastname())
                    .phoneNumber(user.getPhoneNumber())
                    .categoryName(user.getGroup().getCategory().getName())
                    .active(user.isActive())
                    .build();
            resStudentDTOList.add(resStudentDTO);
        }
        return new ApiResponse("All students successfully retrieved", HttpStatus.OK, resStudentDTOList);
    }



    public ApiResponse updateStudent(UUID id,ReqStudent studentDTO) {
        User student = userRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Student not found").statusCode(404).build());
        Group group = groupRepository.findById(studentDTO.getGroupId())
                .orElseThrow(() -> GenericException.builder().message("Group not found").statusCode(404).build());
            student.setFirstname(studentDTO.getFirstname());
            student.setLastname(studentDTO.getLastname());
            student.setPhoneNumber(studentDTO.getPhoneNumber());
            student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
            student.setGroup(group);
            student.setRole(ERole.ROLE_STUDENT);
            userRepository.save(student);
            return new ApiResponse("Student successfully updated", HttpStatus.OK);
    }


    public ApiResponse deleteStudent(UUID id){
        User student = userRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Student not found").statusCode(404).build());
        userRepository.delete(student);
        return new ApiResponse("Student successfully deleted", HttpStatus.OK);
    }


    public ApiResponse updateActiveInStudent(UUID id, boolean active){
        User student = userRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Student not found").statusCode(404).build());
        student.setActive(active);
        userRepository.save(student);
        return new ApiResponse("Student successfully updated", HttpStatus.OK,null);
    }


//    Teacherni uzining studentlarini top reytingi
    public ApiResponse getTopStudentByTeacher(User teacher){

        Map<ResStudent, Integer> topStudentMap = new HashMap<>();
        List<User> activeStudents = userRepository.findAllByRoleAndGroup_Teacher(ERole.ROLE_STUDENT,teacher);
        if (!activeStudents.isEmpty()) {
            for (User user : activeStudents) {
                if (user.isActive()) {
                    Integer score = homeWorkService.getTotalScoreByStudentsAndCurrentMonth(user);
                    Integer ratingStudent = homeWorkRepository.getRatingStudent(user.getGroup().getId(), user.getId());
                    ResStudent resStudent = ResStudent.builder()
                            .fullName(user.getFirstname() + " " + user.getLastname())
                            .categoryName(user.getGroup().getCategory().getName())
                            .groupName(user.getGroup().getName())
                            .ball(score)
                            .rate(ratingStudent)
                            .build();
                    topStudentMap.put(resStudent, score);
                }
            }

            List<ResStudent> topStudens = topStudentMap.entrySet().stream()
                    .sorted(Map.Entry.<ResStudent, Integer>comparingByValue().reversed())
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .toList();

            return new ApiResponse("Success", true, HttpStatus.OK, topStudens);
            }
        return new ApiResponse("Failed",  HttpStatus.BAD_REQUEST);
    }

    public ApiResponse getStudentsHomework(UUID studentId, Integer lessonId){
        List<StudentHomeworkDTO> studentsHomeworks = homeWorkRepository.getStudentsHomeworks(studentId, lessonId);
        return new ApiResponse("Success", HttpStatus.OK, studentsHomeworks);
    }

    public ApiResponse getStudentList(User user){
        List<StudentListDto> studentList = new ArrayList<>();
        for (User users : homeWorkRepository.getStudentList(user.getId())) {
            Integer lessonByStudent = lessonRepository.findLessonByStudent(users.getId());
            StudentListDto student = StudentListDto.builder()
                    .studentId(users.getId())
                    .fullName(users.getFirstname()+" "+users.getLastname())
                    .lessonId(lessonByStudent)
                    .build();
            studentList.add(student);
        }
        return new ApiResponse("Success", HttpStatus.OK, studentList);
    }
}
