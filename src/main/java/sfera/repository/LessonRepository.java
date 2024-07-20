package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.*;

import java.util.List;
import java.util.Optional;

import sfera.entity.Module;
import sfera.payload.LessonsDTO;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsByNameAndModuleNot(String name, Module module);

    @Query(value = "select count(*) from lesson where module_id in :moduleId", nativeQuery = true)
    int findCountLesson(List<Integer> moduleId);
    Optional<Lesson> findLessonByTaskList(List<Task> taskList);

    Integer countByModule(Module module);

    List<Lesson> findByModule(Module module);
    List<Lesson> findAllByModule_Category(Category category);

    @Query(value = "select m.order_name as moduleName, l.id as lessonId, l.name as lessonName, t.active as active " +
            "from users as u inner join groups as g on u.group_id = g.id " +
            "inner join category as c on g.category_id=c.id inner join module as m on c.id = m.category_id " +
            "inner join lesson as l on m.id = l.module_id left join lesson_tracking as t on l.id = t.lesson_id " +
            "where u.id=:userId", nativeQuery = true)
    List<LessonsDTO> findAllStudentsLessons(@Param("userId") UUID userId);

    @Query(value = "select l.*, v.file_name from  lesson_video_file as lvf " +
            "right join lesson as l ON lvf.lesson_id = l.id " +
            "left join video_file as v ON lvf.video_file_id = v.id where l.id=:lessonId", nativeQuery = true)
    Lesson findStudentsLesson(@Param("lessonId") Integer lessonId);


    @Query(value = "SELECT l.id FROM lesson l " +
            "JOIN lesson_tracking lt ON l.id = lt.lesson_id " +
            "JOIN groups g ON lt.group_id = g.id " +
            "JOIN users s ON s.group_id = g.id " +
            "WHERE s.id = :studentId", nativeQuery = true)
    Integer findLessonByStudent(@Param("studentId") UUID studentId);




}
