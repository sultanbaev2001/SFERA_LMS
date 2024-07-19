package sfera.payload.top;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopTeacherDTO {
    private UUID teacherId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer score;
}
