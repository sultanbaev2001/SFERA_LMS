package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.User;
import sfera.entity.enums.ERole;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Integer countByRoleAndActiveTrue(ERole role);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
