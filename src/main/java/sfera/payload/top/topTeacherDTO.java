package sfera.payload.top;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class topTeacherDTO {
    private Integer teacherId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String categoryName;
    private Integer rating;
}
