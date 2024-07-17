package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Module;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    boolean existsByOrderName(String name);

    List<Module> findAllByCategory_Id(Integer categoryId);

    Module findByOrderName(String name);
}
