package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}