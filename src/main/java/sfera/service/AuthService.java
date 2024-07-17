package sfera.service;

import sfera.entity.User;
import sfera.exception.UserNotFoundException;
import sfera.payload.ApiResponse;
import sfera.payload.auth.AuthLogin;
import sfera.repository.UserRepository;
import sfera.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.repository.UserRepository;
import sfera.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse login(AuthLogin authLoginDTO) {
        User user = userRepository.findByPhoneNumber(authLoginDTO.getPhoneNumber())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if(passwordEncoder.matches(authLoginDTO.getPassword(), user.getPassword())) {
            String token = jwtProvider.generateToken(authLoginDTO.getPhoneNumber());
            return new ApiResponse(token, true, HttpStatus.OK,null);
        }
        return new ApiResponse("password do not match", false,HttpStatus.UNAUTHORIZED,null);
    }

}
