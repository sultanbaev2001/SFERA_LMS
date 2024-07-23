package sfera.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sfera.entity.LessonTracking;
import sfera.payload.req.ReqLessonTracking;
import sfera.payload.res.ResLessonTracking;

@Mapper(componentModel = "spring")
public interface LessonTrackingMapper {

    // Mapping from ReqLessonTracking to LessonTracking entity
    @Mapping(source = "ltId", target = "id")
    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "active", target = "active")
    LessonTracking toLessonTracking(ReqLessonTracking reqLessonTracking);

    // Mapping from LessonTracking entity to ResLessonTracking response
    @Mapping(target = "ltId", source = "id")
    @Mapping(target = "groupName", source = "group.name")
    @Mapping(target = "lessonName", source = "lesson.name")
    @Mapping(target = "active", source = "active")
    ResLessonTracking toResLessonTracking(LessonTracking lessonTracking);
}
