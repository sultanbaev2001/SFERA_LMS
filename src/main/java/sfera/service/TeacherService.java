package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.entity.*;
import sfera.entity.enums.ERole;
import sfera.payload.ApiResponse;
import sfera.payload.teacher_homework.HomeworkDTO;
import sfera.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final HomeWorkRepository homeWorkRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;
    private final LessonTrackingRepository lessonTrackingRepository;



    public ApiResponse getStudentHomework(User teacher, User student, Integer lesson){



        return null;
    }
}
