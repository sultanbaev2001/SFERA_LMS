package sfera.payload.res;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResGroup {

    private Integer id;
    private String name;
    private String categoryName;
    private boolean active;
}
