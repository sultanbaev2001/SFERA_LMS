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
    private Integer moduleId;
    private List<TaskDto> taskDtoList;
    private List<Long> fileIds;
}
