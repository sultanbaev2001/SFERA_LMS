package sfera.payload.top;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopTeacherDTO {
    private Long teacherId;
    private String fullName;
    private String phoneNumber;
    private Integer score;
}
