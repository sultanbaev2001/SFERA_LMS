package sfera.payload.req;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqLessonTracking {

    private Integer ltId;

    private Integer groupId;

    private Integer lessonId;

    private boolean isActive;


}
