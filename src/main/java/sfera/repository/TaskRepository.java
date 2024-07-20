package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {


    @Query(value ="select * from lesson_task_list as lt inner join task as t on lt.task_id=t.id where lt.lesson_id=:lessonId" ,nativeQuery = true)
    List<Task> getAllLessonTasks(@Param("lessonId") Integer lessonId);


    List<Task> getOpenTasks(@Param("userId")UUID userId);
}