package sfera.payload;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    private UUID id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;
    private Integer groupId;
}
