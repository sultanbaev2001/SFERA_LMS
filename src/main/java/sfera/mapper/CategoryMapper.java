package sfera.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sfera.entity.Category;
import sfera.payload.CategoryDTO;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

//    DTO to ENTITY
    @Mapping(source = "categoryId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "isActive", target = "isActive")
    Category toEntity(CategoryDTO categoryDTO);


//    ENTITY to DTO
    @Mapping(source = "id", target = "categoryId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "isActive", target = "isActive")
    CategoryDTO toDTO(Category category);
}