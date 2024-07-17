package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Group;
import sfera.entity.HomeWork;
import sfera.entity.User;
import java.time.LocalDate;
import java.util.List;

public interface HomeWorkRepository extends JpaRepository<HomeWork, Integer> {

    @Query("SELECT SUM(hw.score) FROM HomeWork hw WHERE hw.student IN :student AND hw.dueDate >= :startDate AND hw.dueDate <= :endDate")
    Integer findTotalScoreByStudentsAndPeriod(@Param("student") User student, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<HomeWork> findAllByDueDateBetweenAndStudent(LocalDate startDate, LocalDate endDate,User user);

    @Query("SELECT SUM(hw.score) FROM HomeWork hw WHERE hw.student.group = :group AND hw.dueDate >= :startDate AND hw.dueDate <= :endDate")
    Integer findTotalScoreByGroupAndPeriod(@Param("group") Group group, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<HomeWork> findAllByDueDateBetweenAndStudent(LocalDate startDate, LocalDate endDate, User user);

    @Query(value = "select * from home_work where task_id in : taskIds", nativeQuery = true)
    List<HomeWork> getAllHomework(@Param("taskIds") List<Integer> taskIds);
}
