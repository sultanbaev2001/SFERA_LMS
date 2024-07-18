package sfera.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDTO {
    private String name;
    private String moduleName;
    private String categoryName;
    private List<TaskDto> taskDtoList;
    private List<String> videoFileName;
}
