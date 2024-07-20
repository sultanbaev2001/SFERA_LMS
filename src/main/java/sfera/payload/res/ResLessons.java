package sfera.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResLessons {

    private int lessonId;
    private String lessonName;
}
