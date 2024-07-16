package sfera.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticDto {

    private Integer teacherCount;
    private Integer studentCount;
    private Integer categoryCount;
    private Integer groupCount;
}
