package sfera.controller;

import sfera.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sfera.payload.auth.AuthLogin;
import sfera.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public HttpEntity<ApiResponse> logIn(@RequestBody AuthLogin authLoginDTO){
        ApiResponse login = authService.login(authLoginDTO);
        return ResponseEntity.status(login.getStatus()).body(login);
    }
}
