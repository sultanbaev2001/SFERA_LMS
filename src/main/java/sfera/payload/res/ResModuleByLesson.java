package sfera.payload.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResModuleByLesson {

    private String moduleName;
    List<ResLessons> resLessonList;
}
