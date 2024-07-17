package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.entity.Group;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.exception.UserNotFoundException;
import sfera.payload.ApiResponse;
import sfera.payload.TeacherDto;
import sfera.payload.req.ReqTeacher;
import sfera.payload.res.ResGroupStudentCount;
import sfera.payload.res.ResTeacher;
import sfera.repository.GroupRepository;
import sfera.exception.GenericException;
import sfera.payload.StudentDTO;
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
        return new ApiResponse("Successfully saved user",true, HttpStatus.OK,null);
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
        List<Group> groups = groupRepository.findAllByTeacher(user);
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
        TeacherDto.builder()
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .active(user.isActive())
                .resGroupStudentCount(resGroupStudentCounts)
                .categoryName(categoryName)
                .build();
        return new ApiResponse("Success",true, HttpStatus.OK,resGroupStudentCounts);
    }

    public  ApiResponse deActiveTeacher(UUID teacherId, Boolean active){
        User user = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        user.setActive(active);
        return new ApiResponse("Successfully edited user",true, HttpStatus.OK,user);
    }

    public ApiResponse editTeacher(UUID teacherId, ReqTeacher reqTeacher){
        User user = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        user.setFirstname(reqTeacher.getFirstName());
        user.setLastname(reqTeacher.getLastName());
        user.setPhoneNumber(reqTeacher.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(reqTeacher.getPassword()));
        userRepository.save(user);
        return new ApiResponse("Successfully edited user",true, HttpStatus.OK,user);
    }


    public ApiResponse saveStudent(StudentDTO studentDTO) {
        boolean existsed = userRepository.existsByPhoneNumberAndIdNot(studentDTO.getPhoneNumber(),studentDTO.getId());
        Group group = groupRepository.findById(studentDTO.getGroupId())
                .orElseThrow(() -> GenericException.builder().message("Group not found").statusCode(404).build());
        if (!existsed){
            User user = User.builder()
                    .firstname(studentDTO.getFirstname())
                    .lastname(studentDTO.getLastname())
                    .phoneNumber(studentDTO.getPhoneNumber())
                    .password(passwordEncoder.encode(studentDTO.getPassword()))
                    .group(group)
                    .role(ERole.ROLE_STUDENT)
                    .build();
            userRepository.save(user);
            return new ApiResponse("Student successfully saved", HttpStatus.OK);
        }
        return new ApiResponse("Student already exists", HttpStatus.CONFLICT);
    }

    public ApiResponse getAllStudents() {
        List<User> users = userRepository.findAll();
        List<StudentDTO> studentDTOList= new ArrayList<>();
        for (User user : users){
            if (user.getRole().equals(ERole.ROLE_STUDENT)){
                StudentDTO studentDTO= StudentDTO.builder()
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .phoneNumber(user.getPhoneNumber())
                        .groupId(user.getGroup().getId())
                        .build();
                studentDTOList.add(studentDTO);
            }
        }
        return new ApiResponse("All students successfully retrieved", HttpStatus.OK, studentDTOList);
    }


    public ApiResponse updateStudent(StudentDTO studentDTO) {
        boolean existsed = userRepository
                .existsByPhoneNumberAndIdNot(studentDTO.getPhoneNumber(), studentDTO.getId());
        User student = userRepository.findById(studentDTO.getId())
                .orElseThrow(() -> GenericException.builder().message("Student not found").statusCode(404).build());
        Group group = groupRepository.findById(studentDTO.getGroupId())
                .orElseThrow(() -> GenericException.builder().message("Group not found").statusCode(404).build());
        if (!existsed){
            student.setFirstname(studentDTO.getFirstname());
            student.setLastname(studentDTO.getLastname());
            student.setPhoneNumber(studentDTO.getPhoneNumber());
            student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
            student.setGroup(group);
            student.setRole(ERole.ROLE_STUDENT);
            userRepository.save(student);
            return new ApiResponse("Student successfully updated", HttpStatus.OK);
        }
        return new ApiResponse("Student already exists", HttpStatus.CONFLICT);
    }


    public ApiResponse deleteStudent(UUID id){
        User student = userRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Student not found").statusCode(404).build());
        userRepository.delete(student);
        return new ApiResponse("Student successfully deleted", HttpStatus.OK);
    }
}
