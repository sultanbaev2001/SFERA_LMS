package sfera.payload.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResLessonTracking {

    private Integer ltId;

    private String groupName;

    private String lessonName;

    private boolean active;

}
