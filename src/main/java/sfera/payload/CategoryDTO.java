package sfera.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
        private Integer categoryId;
        private String name;
        private String description;
        private boolean isActive;
}
