package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.entity.Group;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.StudentDTO;
import sfera.repository.GroupRepository;
import sfera.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

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
