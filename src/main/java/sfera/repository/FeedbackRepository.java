package sfera.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.entity.Feedback;
import sfera.entity.Group;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query(value = "SELECT * FROM feedback WHERE teacher_id = :teacherId", nativeQuery = true)
    List<Feedback> findByTeacherId(@Param("teacherId") UUID teacherId);

    int countByTeacherId(UUID teacherId);
}
