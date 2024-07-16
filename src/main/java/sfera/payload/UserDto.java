package sfera.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;

}
