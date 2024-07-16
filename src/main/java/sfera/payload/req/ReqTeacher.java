package sfera.payload.req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqTeacher {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;

}
