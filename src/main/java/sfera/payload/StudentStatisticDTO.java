package sfera.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentStatisticDTO {
    private Integer availableLessons;
    private Integer countAllLessons;
    private Integer score;
    private Integer ratingStudent;
    private Integer countRatingStudents;
}
