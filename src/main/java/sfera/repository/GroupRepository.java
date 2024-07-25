package sfera.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Category;
import sfera.entity.Group;
import sfera.entity.User;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Integer countByActiveTrue();

    List<Group> findAllByTeacherId(Long teacherId);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, int id);

    List<Group> findAllByActiveTrue();

    List<Group> findAllByCategory(Category category);
}
