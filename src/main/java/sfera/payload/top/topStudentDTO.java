package sfera.payload.top;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class topStudentDTO {
    private Integer studentId;
    private String firstName;
    private String lastName;
    private String groupName;
    private Integer ball;
    private Integer rating;
}
