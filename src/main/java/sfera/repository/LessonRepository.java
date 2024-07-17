package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Lesson;
import sfera.entity.Module;
import sfera.entity.Task;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsByNameAndModuleNot(String name, Module module);
}
