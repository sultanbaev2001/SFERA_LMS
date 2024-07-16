package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.exception.UserNotFoundException;
import sfera.payload.ApiResponse;
import sfera.payload.UserDto;
import sfera.payload.res.ResTeacher;
import sfera.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse saveTeacher(UserDto userDto){
        boolean exists = userRepository.existsByPhoneNumber(userDto.getPhoneNumber());
        if(exists){
            return new ApiResponse("Phone number already exits",false, HttpStatus.BAD_REQUEST,null);
        }
        User user=User.builder()
                .firstname(userDto.getFirstName())
                .lastname(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(passwordEncoder.encode(userDto.getPassword()))
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
                    .build();
            resTeachers.add(resTeacher);
        }
        return new ApiResponse("Successfully retrieved users",true, HttpStatus.OK,resTeachers);
    }

    public ApiResponse getTeacher(UUID teacherId){
        User user = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
        return null;
    }


}
