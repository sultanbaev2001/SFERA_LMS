package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.entity.Category;
import sfera.entity.Group;
import sfera.entity.User;
import sfera.entity.enums.ERole;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Integer countByRoleAndActiveTrue(ERole role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Integer countAllByGroup_CategoryAndRoleAndActiveTrue(Category category, ERole role);
}
