package sfera.payload;

import lombok.*;
import sfera.payload.res.ResGroupStudentCount;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean active;
    private List<ResGroupStudentCount> resGroupStudentCount;
    private Set<String> categoryName;




}
