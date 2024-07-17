package sfera.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResLesson {
    private Integer lessonId;
    private String name;
    private String moduleName;
    private String categoryName;
}
