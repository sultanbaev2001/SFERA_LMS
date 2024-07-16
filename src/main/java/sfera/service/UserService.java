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
import sfera.repository.UserRepository;

import java.util.*;

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
        List<User> users = userRepository.findUser();
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


}
