package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Category;
import sfera.entity.User;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}
