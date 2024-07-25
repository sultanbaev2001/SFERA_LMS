package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Module;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    boolean existsByOrderName(String name);

    @Query(value = "select id from module where category_id=:categoryId", nativeQuery = true)
    List<Integer> findAllIds(@Param("categoryId") Integer categoryId);

    List<Module> findAllByCategory_Id(Integer categoryId);

    Module findByOrderName(String name);
    List<Module> findAllByTeacherId(Long teacherId);

}
