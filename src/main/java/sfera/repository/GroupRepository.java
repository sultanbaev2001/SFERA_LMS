package sfera.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Integer countByActiveTrue();
}
