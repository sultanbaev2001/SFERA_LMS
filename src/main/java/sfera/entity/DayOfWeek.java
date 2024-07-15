package sfera.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sfera.entity.enums.EDayOfWeek;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DayOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private EDayOfWeek dayOfWeek;
}
