package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value ="select * from lesson_task_list as lt where lt.lesson_id=:lessonId" ,nativeQuery = true)
    List<Task> getAllLessonTasks(@Param("lessonId") Integer lessonId);

    @Query(value = "select t.* from lesson_tracking as lt " +
            "join lesson_task_list as ltl on lt.lesson_id=ltl.lesson_id " +
            "join task as t on ltl.task_list_id = t.id " +
            "where lt.group_id=:groupId and lt.active is true and " +
            "t.id not in (select task_id from home_work where student_id=:studentId); ",nativeQuery = true)
    List<Task> studentTasks(@Param("groupId") Integer groupId, @Param("studentId") Long studentId);




}