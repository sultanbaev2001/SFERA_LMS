package sfera.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDTO {
    private Integer id;
    private String name;
    private List<Long> fileIds;
    private List<TaskDto> tasks;
}
