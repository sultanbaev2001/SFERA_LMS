package sfera.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleDTO {
    private Integer moduleId;
    private String orderName;
    private Integer categoryId;
}
