package sfera.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResStudent {
    private String fullName;
    private String phoneNumber;
    private String password;
    private String groupName;
    private String categoryName;
}
