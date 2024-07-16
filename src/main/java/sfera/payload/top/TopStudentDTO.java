package sfera.payload.top;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopStudentDTO {
    private UUID studentId;
    private String firstName;
    private String lastName;
    private String groupName;
    private Integer ball;
    private Integer rating;
}
