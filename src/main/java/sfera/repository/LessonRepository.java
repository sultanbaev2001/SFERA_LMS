package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.entity.Category;
import sfera.entity.Lesson;
import sfera.entity.Task;

import java.util.List;
import java.util.Optional;
import sfera.entity.Module;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsByNameAndModuleNot(String name, Module module);

    @Query(value = "select count(*) from lesson where module_id in :moduleId", nativeQuery = true)
    int findCountLesson(List<Integer> moduleId);
    Optional<Lesson> findLessonByTaskList(List<Task> taskList);

    List<Lesson> findAllByModule_Category(Category category);


}
