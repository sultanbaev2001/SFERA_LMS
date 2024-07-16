package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Integer countByIsActiveTrue();

    List<Category> findByIsActiveTrue();
    boolean existsByName(String name);
}
