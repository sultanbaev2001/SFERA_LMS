package sfera.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResStudentDTO {
    private String fullName;
    private String categoryName;
    private String phoneNumber;
    private boolean active;
}
