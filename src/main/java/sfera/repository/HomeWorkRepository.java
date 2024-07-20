package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Group;
import sfera.entity.HomeWork;
import sfera.entity.User;
import sfera.payload.res.CategoryStatistics;
import sfera.payload.res.GroupStatistics;

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

    @Query(value = "SELECT new sfera.payload.res.CategoryStatistics(c.name, EXTRACT(MONTH FROM hw.dueDate), SUM(hw.score)) " +
            "FROM HomeWork hw " +
            "JOIN users as u on hw.student_id=u.id " +
            "JOIN groups as g on u.group_id=g.id " +
            "JOIN category as c on c.id=g.category_id" +
            "GROUP BY c.name, EXTRACT(MONTH FROM hw.dueDate) " +
            "ORDER BY c.name, EXTRACT(MONTH FROM hw.dueDate)", nativeQuery = true)
    List<CategoryStatistics> findCategoryStatistics();

    @Query(value="select new sfera.payload.res.GroupStatistics(g.name, EXTRACT(MONTH FROM hw.dueDate), SUM(hw.score))"+
                "FROM HomeWork hw "+
                "JOIN users as u on hw.student_id=u.id"  +
                "JOIN groups as g on u.group_id=g.id "+
                "GROUP BY c.name, EXTRACT(MONTH FROM hw.dueDate) " +
                "ORDER BY c.name, EXTRACT(MONTH FROM hw.dueDate)", nativeQuery = true)
    List<GroupStatistics> findGroupStatistics();

}
