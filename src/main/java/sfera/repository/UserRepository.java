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

public interface UserRepository extends JpaRepository<User, Long> {

    Integer countByRoleAndActiveTrue(ERole role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAllByGroup_CategoryAndRoleAndActiveTrue(Category category, ERole role);

    boolean existsByPhoneNumber(String phoneNumber);


    Integer countAllByGroupAndRole(Group group, ERole role);


    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);


    List<User> findByRole(ERole role);


    @Query(value = "SELECT u FROM User u WHERE u.active=true AND u.role='ROLE_STUDENT' AND " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :text, '%')) OR "+
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :text,'%'))" )
    List<User> searchByText(@Param("text") String text);

    List<User> findAllByGroupAndRoleAndActiveTrue(Group group, ERole role);
    List<User> findAllByGroupId(Integer groupId);
    List<User> findAllByRoleAndGroup_Teacher(ERole role, User teacher);

    @Query(value = "SELECT u.* FROM users u " +
            "INNER JOIN groups g ON g.id = u.group_id " +
            "INNER JOIN category c ON c.id = g.category_id " +
            "WHERE (?1 IS NULL OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR ?1 IS NULL OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', ?1, '%'))) " +
            "AND (?2 IS NULL OR g.id = ?2) " +
            "AND (?3 IS NULL OR c.id = ?3)", nativeQuery = true)
    List<User> findAlLByStudentSearchAndGroupIdAndCategoryId(String name, Integer groupId, Integer categoryId);
    ;

}
