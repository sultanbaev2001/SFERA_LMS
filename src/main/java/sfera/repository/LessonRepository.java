package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

}
