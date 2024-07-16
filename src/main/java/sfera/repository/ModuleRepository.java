package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    boolean existsByOrderName(String name);
}
