package sfera.payload.top;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopStudentDTO {
    private Long studentId;
    private String fullName;
    private String groupName;
    private Integer score;
}
