package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.LessonTracking;

import java.util.UUID;

public interface LessonTrackingRepository extends JpaRepository<LessonTracking, Integer> {

    @Query(value = "select count(id) from lesson_tracking where group_id=:groupId and available is true", nativeQuery = true)
    int countAllAvailableLessons(@Param("groupId") Integer groupId);

}
