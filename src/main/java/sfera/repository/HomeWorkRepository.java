package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Group;
import sfera.entity.HomeWork;

import sfera.entity.User;
import sfera.payload.teacher_homework.StudentHomeworkDTO;
import sfera.payload.StudentRatingDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HomeWorkRepository extends JpaRepository<HomeWork, Integer> {

    @Query("SELECT SUM(hw.score) FROM HomeWork hw WHERE hw.student IN :student AND hw.dueDate >= :startDate AND hw.dueDate <= :endDate")
    Integer findTotalScoreByStudentsAndPeriod(@Param("student") User student, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select sum(score) from home_work where student_id=:studentId", nativeQuery = true)
    int findAllScoreByStudent(@Param("studentId") UUID studentId);

    @Query(value = "select count(*) as count  from (select sum(score) from home_work h" +
            " inner join users u on h.student_id = u.id where u.group_id=:groupId group by u) as subquery", nativeQuery = true)
    Integer getCountRatingStudents(@Param("groupId") Integer groupId);

    @Query(value = "WITH ranked_users AS (\n" +
            "    SELECT u.id,\n" +
            "           SUM(h.score) AS total_score,\n" +
            "           ROW_NUMBER() OVER (ORDER BY SUM(h.score) DESC) AS position\n" +
            "    FROM home_work h\n" +
            "             INNER JOIN users u ON h.student_id = u.id\n" +
            "    WHERE u.group_id = :groupId\n" +
            "    GROUP BY u.id\n" +
            ")\n" +
            "SELECT position\n" +
            "FROM ranked_users\n" +
            "WHERE id = :userId;" ,nativeQuery = true)
    Integer getRatingStudent(@Param("groupId") Integer groupId, @Param("userId") UUID userId);

    List<HomeWork> findAllByDueDateBetweenAndStudent(LocalDate startDate, LocalDate endDate,User user);

    @Query(value = "select * from home_work where task_id in : taskIds", nativeQuery = true)
    List<HomeWork> getAllHomework(@Param("taskIds") List<Integer> taskIds);
    @Query("SELECT SUM(hw.score) FROM HomeWork hw WHERE hw.student.group = :group AND hw.dueDate >= :startDate AND hw.dueDate <= :endDate")
    Integer findTotalScoreByGroupAndPeriod(@Param("group") Group group, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



    @Query(value = "SELECT u.firstname AS firstname, u.lastname AS lastname, u.phone_number AS phoneNumber, " +
            "SUM(h.score) AS score " +
            "FROM home_work h " +
            "INNER JOIN users u ON h.student_id = u.id " +
            "WHERE u.group_id = :groupId " +
            "GROUP BY u.firstname, u.lastname, u.phone_number " +
            "ORDER BY score DESC", nativeQuery = true)
    List<StudentRatingDTO> getRatingStudents(@Param("groupId") Integer groupId);


    @Query(value = "select concat(s.firstname, s,lastname) as fullName, t.name, g.name, " +
            "m.order_name, hw.due_date, from users as s " +
            "inner join groups as g on s.group_id=g.id " +
            "inner join lesson_tracking as lt on g.id = lt.group_id " +
            "inner join lesson as l on l.id = lt.lesson_id " +
            "inner join module as m on l.module_id = m.id " +
            "inner join lesson_task_list as ltl on l.id = ltl.lesson_id " +
            "inner join task as t on ltl.task_list_id = t.id " +
            "inner join home_work as hm on t.id = hm.task_id where hm.student_id=:studentId " +
            "and hm.score is null and l.id=:lessonId" , nativeQuery = true)
    List<StudentHomeworkDTO> getStudentsHomeworks(@Param("studentId") UUID studentId, @Param("lessonId") Integer lessonId);


    @Query(value = "SELECT s.* FROM user AS s " +
            "INNER JOIN home_work AS hm ON hm.student_id = s.id " +
            "INNER JOIN groups AS g ON g.id = s.group_id " +
            "WHERE hm.score IS NULL " +
            "AND g.teacher_id = :teacherId", nativeQuery = true)
    List<User> getStudentList(@Param("teacherId") UUID teacherId);




    List<HomeWork> findAllByTaskId(Integer taskId);


}
