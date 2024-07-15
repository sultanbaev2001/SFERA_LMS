package sfera.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    private Category category;
    @OneToMany
    private List<DayOfWeek> days;
    @ManyToOne
    private User teacherId;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isActive;
}
