package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.entity.LessonTracking;

import java.util.UUID;

public interface LessonTrackingRepository extends JpaRepository<LessonTracking, Integer> {

    @Query(value = "select count(id) from lesson_tracking where group_id=:groupId and is_available is true")
    int countAllAvailableLessons(Integer groupId, UUID userId);

}
