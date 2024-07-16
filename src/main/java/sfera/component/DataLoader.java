package sfera.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sfera.entity.User;
import sfera.entity.enums.ERole;
import sfera.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args)  {
        if (userRepository.count()==0) {
            User newUser = new User();
            newUser.setFirstname("Admin");
            newUser.setLastname("Admin");
            newUser.setPassword(passwordEncoder.encode("root123"));
            newUser.setRole(ERole.ROLE_ADMIN);
            newUser.setPhoneNumber("+998905789900");
            newUser.setEnabled(true);
            newUser.setAccountNonExpired(true);
            newUser.setAccountNonLocked(true);
            newUser.setCredentialsNonExpired(true);
            userRepository.save(newUser);
        }
    }
}
