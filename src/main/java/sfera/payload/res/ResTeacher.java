package sfera.payload.res;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResTeacher {

    private Long teacherID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean active;
}
