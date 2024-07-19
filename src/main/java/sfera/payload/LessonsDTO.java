package sfera.payload;

import lombok.*;
import org.springframework.data.domain.PageRequest;

import java.sql.Time;
import java.sql.Timestamp;

public interface LessonsDTO {

    Integer getModuleId();
    String getModuleName();
    String getLessonName();
    Integer getLessonId();
    String getUpdatedAt();
    Boolean getActive();
}
