package sfera.payload.res;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResTeacher {

    private UUID teacherID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
