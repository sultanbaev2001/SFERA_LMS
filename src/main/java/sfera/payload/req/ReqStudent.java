package sfera.payload.req;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqStudent {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;
    private Integer groupId;
}
