package sfera.payload.res;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupStatistics {
    private String groupName;
    private int month;
    private double totalScore;
}
