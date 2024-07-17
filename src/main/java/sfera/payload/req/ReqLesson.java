package sfera.payload.req;

import lombok.*;
import sfera.payload.TaskDto;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqLesson {
    private Integer id;
    private String name;
    private String moduleName;
    private String categoryName;
    private List<TaskDto> taskDtoList;
    private List<String> videoFileName;
}
