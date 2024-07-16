package sfera.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResCategory {

    private String categoryName;
    private double percentage;
}
