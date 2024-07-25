package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Group;
import sfera.entity.HomeWork;
import sfera.entity.User;
import sfera.payload.StudentRatingDTO;
import sfera.payload.res.CategoryStatistics;
import sfera.payload.res.GroupStatistics;
import sfera.payload.teacher_homework.StudentHomeworkDTO;

import java.time.LocalDate;
import java.util.List;


public interface HomeWorkRepository extends JpaRepository<HomeWork, Integer> {

    @Query("SELECT SUM(hw.score) FROM HomeWork hw WHERE hw.student IN :student AND hw.dueDate >= :startDate AND hw.dueDate <= :endDate AND hw.score IS NOT NULL")
    Integer findTotalScoreByStudentsAndPeriod(@Param("student") User student, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select sum(score) from home_work where student_id=:studentId", nativeQuery = true)
    int findAllScoreByStudent(@Param("studentId") Long studentId);

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
    Integer getRatingStudent(@Param("groupId") Integer groupId, @Param("userId") Long userId);

    List<HomeWork> findAllByDueDateBetweenAndStudent(LocalDate startDate, LocalDate endDate,User user);

    @Query(value = "select * from home_work where task_id in : taskIds", nativeQuery = true)
    List<HomeWork> getAllHomework(@Param("taskIds") List<Integer> taskIds);
    @Query("SELECT SUM(hw.score) FROM HomeWork hw WHERE hw.student.group = :group AND hw.dueDate >= :startDate AND hw.dueDate <= :endDate")
    Integer findTotalScoreByGroupAndPeriod(@Param("group") Group group, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new sfera.payload.res.CategoryStatistics(c.name, EXTRACT(MONTH FROM hw.dueDate) as month, SUM(hw.score) as totalScore) " +
            "FROM HomeWork hw " +
            "JOIN hw.student u " +
            "JOIN u.group g " +
            "JOIN g.category c " +
            "WHERE hw.score IS NOT NULL "+
            "GROUP BY c.name, month " +
            "ORDER BY c.name, month")
    List<CategoryStatistics> findCategoryStatistics();

    @Query("SELECT new sfera.payload.res.GroupStatistics(g.name, EXTRACT(MONTH FROM hw.dueDate) as month, SUM(hw.score) as totalScore) " +
            "FROM HomeWork hw " +
            "JOIN hw.student u " +
            "JOIN u.group g " +
            "WHERE hw.score IS NOT NULL "+
            "GROUP BY g.name, month " +
            "ORDER BY g.name, month")
    List<GroupStatistics> findGroupStatistics();

    @Query("SELECT new sfera.payload.res.GroupStatistics(g.name, EXTRACT(MONTH FROM hw.dueDate) as month, SUM(hw.score) as totalScore) " +
            "FROM HomeWork hw " +
            "JOIN hw.student u " +
            "JOIN u.group g WHERE g.teacher.id=:teacherId AND hw.score IS NOT NULL " +
            "GROUP BY g.name, month " +
            "ORDER BY g.name, month")
    List<GroupStatistics> findGroupStatisticsByTeacher(@Param("teacherId") Long teacherId);



    @Query(value = "SELECT u.firstname AS firstname, u.lastname AS lastname, u.phone_number AS phoneNumber, " +
            "SUM(h.score) AS score " +
            "FROM home_work h " +
            "INNER JOIN users u ON h.student_id = u.id " +
            "WHERE u.group_id = :groupId " +
            "GROUP BY u.firstname, u.lastname, u.phone_number " +
            "ORDER BY score DESC", nativeQuery = true)
    List<StudentRatingDTO> getRatingStudents(@Param("groupId") Integer groupId);


    @Query(value = "select concat(s.firstname, s,lastname) as fullName, t.name, g.name, " +
            "m.order_name, hm.due_date from users as s " +
            "inner join groups as g on s.group_id=g.id " +
            "inner join lesson_tracking as lt on g.id = lt.group_id " +
            "inner join lesson as l on l.id = lt.lesson_id " +
            "inner join module as m on l.module_id = m.id " +
            "inner join lesson_task_list as ltl on l.id = ltl.lesson_id " +
            "inner join task as t on ltl.task_list_id = t.id " +
            "inner join home_work as hm on t.id = hm.task_id where hm.student_id=:studentId " +
            "and hm.score is null and l.id=:lessonId" , nativeQuery = true)
    List<StudentHomeworkDTO> getStudentsHomeworks(@Param("studentId") Long studentId, @Param("lessonId") Integer lessonId);

    @Query(value = "SELECT task_id " +
            "FROM home_work " +
            "WHERE task_id IN :ids;",nativeQuery = true)
    List<Integer> getTaskIds(List<Integer> ids);


    @Query(value = "SELECT s.* FROM users AS s " +
            "INNER JOIN home_work AS hm ON hm.student_id = s.id " +
            "INNER JOIN groups AS g ON g.id = s.group_id " +
            "WHERE hm.score IS NULL " +
            "AND g.teacher_id = :teacherId", nativeQuery = true)
    List<User> getStudentList(@Param("teacherId") Long teacherId);


    @Query(value = "UPDATE home_work hw SET score = :inScore WHERE hw.student_id = :studentId AND hw.id = :homeworkId", nativeQuery = true)
    boolean updateHomeWorkByScore(@Param("studentId") Long studentId, @Param("homeworkId") Integer homeworkId, @Param("inScore") Integer inScore);


}
