package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Integer countByIsActiveTrue();
}
