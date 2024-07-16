package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.entity.Category;
import sfera.entity.User;
import sfera.entity.enums.ERole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Integer countByRoleAndActiveTrue(ERole role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Integer countAllByGroup_CategoryAndRoleAndActiveTrue(Category category, ERole role);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.role = 'ROLE_TEACHER' AND u.active = true")
    List<User> findActiveTeachers();

    @Query("SELECT u FROM User u WHERE u.role= 'ROLE_TEACHER'")
    List<User> findUser();
}
