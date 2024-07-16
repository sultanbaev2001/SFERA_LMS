package sfera.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Group;
import sfera.entity.User;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Integer countByActiveTrue();
    List<Group> findAllByTeacher(User teacher);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, int id);
}
