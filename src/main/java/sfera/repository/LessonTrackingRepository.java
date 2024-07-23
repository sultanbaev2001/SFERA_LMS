package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.LessonTracking;

import java.util.List;
import java.util.UUID;

public interface LessonTrackingRepository extends JpaRepository<LessonTracking, Integer> {

    @Query(value = "select count(id) from lesson_tracking where group_id=:groupId and active is true", nativeQuery = true)
    int countAllAvailableLessons(@Param("groupId") Integer groupId);

    List<LessonTracking> findByGroupId(Integer groupId);

    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END " +
            "FROM LessonTracking lt " +
            "WHERE lt.group.id = :groupId AND lt.lesson.id = :lessonId AND lt.active = true")
    boolean existsByGroupIdAndLessonAndActiveTrue(@Param("groupId") Integer groupId,
                                                  @Param("lessonId") Integer lessonId);;

    @Query(value = "select * from lesson_tracking lt " +
            "inner join groups g on g.id = lt.group_id " +
            "where g.teacher_id =: teacherId", nativeQuery = true)
    List<LessonTracking> findAllByTeacherId(@Param("teacherId") UUID teacherId);
}
