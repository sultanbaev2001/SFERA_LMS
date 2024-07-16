package sfera.payload.top;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopGroupDTO {
    private Integer groupId;
    private String groupName;
    private Integer rating;

}
