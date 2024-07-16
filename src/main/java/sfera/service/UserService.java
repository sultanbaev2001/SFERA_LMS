package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.payload.ApiResponse;
import sfera.payload.UserDto;
import sfera.repository.UserRepository;

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
    }


}
