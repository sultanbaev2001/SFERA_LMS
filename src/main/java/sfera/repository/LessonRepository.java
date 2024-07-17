package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.entity.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query(value = "select count(*) from lesson where module_id in :moduleId", nativeQuery = true)
    int findCountLesson(List<Integer> moduleId);
}
