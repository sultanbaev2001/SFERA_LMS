package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.*;
import sfera.exception.GenericException;
import sfera.exception.UserNotFoundException;
import sfera.payload.*;
import sfera.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final HomeWorkRepository homeWorkRepository;
    private final LessonTrackingRepository lessonTrackingRepository;
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public ApiResponse getCountAllAndAvailableLessonsAndScoreAndRate(UUID id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Category category = categoryRepository.findById(user.getGroup().getCategory().getId())
                .orElseThrow(GenericException::new);
        List<Integer> moduleIds = moduleRepository.findAllIds(category.getId());
        Integer countAllLessons = lessonRepository.findCountLesson(moduleIds);
        Integer availableLessons = lessonTrackingRepository.countAllAvailableLessons(user.getGroup().getId());
        Integer allScoreByStudent = homeWorkRepository.findAllScoreByStudent(user.getId());
        Integer countRatingStudents = homeWorkRepository.getCountRatingStudents(user.getGroup().getId());
        Integer ratingStudent = homeWorkRepository.getRatingStudent(user.getGroup().getId(), user.getId());
        StudentStatisticDTO studentStatisticDTO = StudentStatisticDTO.builder()
                .availableLessons(availableLessons)
                .countAllLessons(countAllLessons)
                .score(allScoreByStudent)
                .ratingStudent(ratingStudent)
                .countRatingStudents(countRatingStudents)
                .build();
        return new ApiResponse("Success", true, HttpStatus.OK, studentStatisticDTO);
    }

    public ApiResponse getRatingStudents(User user) {
        Group group = user.getGroup();
        if(group!=null) {
            List<StudentRatingDTO> ratingStudents = homeWorkRepository.getRatingStudents(user.getGroup().getId());
            return new ApiResponse("Success", true, HttpStatus.OK, ratingStudents);
        }
        return new ApiResponse("Group is null",  HttpStatus.NOT_FOUND);
    }

    public ApiResponse getStudentLessons(User user){
        List<LessonsDTO> allStudentsLessons = lessonRepository.findAllStudentsLessons(user.getId());
        return new ApiResponse("Success", HttpStatus.OK, allStudentsLessons);
    }

    public ApiResponse getStudentLesson(Integer lessonId){
        Lesson studentsLesson = lessonRepository.findStudentsLesson(lessonId);
        if(studentsLesson==null){
            return new ApiResponse("Lesson doest not exist", HttpStatus.NOT_FOUND);
        }
        List<Task> allLessonTasks = taskRepository.getAllLessonTasks(lessonId);
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(Task t: allLessonTasks){
            TaskDto taskDto = convertTaskToDto(t);
            taskDtoList.add(taskDto);
        }
        LessonDTO lessonDTO = LessonDTO.builder()
                .id(studentsLesson.getId())
                .name(studentsLesson.getName())
                .files(studentsLesson.getVideoFile().stream().map(VideoFile::getFileName).toList())
                .tasks(taskDtoList)
                .build();
        return new ApiResponse("Success", HttpStatus.OK, lessonDTO);
    }


    public TaskDto convertTaskToDto(Task t){
        return TaskDto.builder()
                .id(t.getId())
                .name(t.getName())
                .description(t.getDescription())
                .files(t.getFiles().stream().map(VideoFile::getFileName).toList())
                .build();
    }
}
