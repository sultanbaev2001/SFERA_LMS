package sfera.payload.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStatistics {
    private String groupName;
    private int month;
    private double totalScore;
}
