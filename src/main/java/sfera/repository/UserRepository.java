package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Category;
import sfera.entity.Group;
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

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.active = true")
    List<User> findActiveRole(@Param("role") ERole role);

    @Query("SELECT u FROM User u WHERE u.role= 'ROLE_TEACHER'")
    List<User> findUser();

    Integer countAllByGroupAndRole(Group group, ERole role);


    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);

    List<User> findAllByGroup(Group group);
}
