package sfera.payload.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLogin {
    private String phoneNumber;
    private String password;
}
