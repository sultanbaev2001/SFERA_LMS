package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Integer countByActiveTrue();

    List<Category> findByActiveTrue();
    boolean existsByName(String name);
}