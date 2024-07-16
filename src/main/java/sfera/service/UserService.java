package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


}
