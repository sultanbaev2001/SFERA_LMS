package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Category;
import sfera.entity.Lesson;
import sfera.entity.Task;

import java.util.List;
import java.util.Optional;
import sfera.entity.Module;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsByNameAndModuleNot(String name, Module module);

    Optional<Lesson> findLessonByTaskList(List<Task> taskList);

    List<Lesson> findAllByModule_Category(Category category);
}
