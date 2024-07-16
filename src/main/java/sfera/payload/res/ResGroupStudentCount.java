package sfera.payload.res;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResGroupStudentCount {

    private String groupName;
    private int studentCount;
}
