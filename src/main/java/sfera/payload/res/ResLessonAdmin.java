package sfera.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResLessonAdmin {

    private int moduleId;
    private String moduleName;
    private int lessonCount;

}
