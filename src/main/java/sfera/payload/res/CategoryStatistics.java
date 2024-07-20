package sfera.payload.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatistics {
    private String categoryName;
    private int month;
    private double totalScore;
}
