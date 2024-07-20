package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.DayOfWeek;

public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Integer> {
}
