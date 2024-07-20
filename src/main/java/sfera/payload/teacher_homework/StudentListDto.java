package sfera.payload.teacher_homework;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentListDto {
    private UUID studentId;
    private String fullName;
    private Integer lessonId;
}
