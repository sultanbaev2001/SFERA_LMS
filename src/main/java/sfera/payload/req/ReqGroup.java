package sfera.payload.req;

import lombok.*;
import sfera.entity.DayOfWeek;


import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqGroup {

    private String name;
    private Integer categoryId;
    private List<DayOfWeek> days;
    private UUID teacherId;
    private String startTime;
    private String endTime;
}
