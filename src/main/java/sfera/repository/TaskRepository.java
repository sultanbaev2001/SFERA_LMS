package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Lesson;
import sfera.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}