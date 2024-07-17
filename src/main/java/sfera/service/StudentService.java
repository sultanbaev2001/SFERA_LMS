package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.User;
import sfera.exception.GenericException;
import sfera.exception.UserNotFoundException;
import sfera.payload.ApiResponse;
import sfera.payload.StudentStatisticDTO;
import sfera.repository.*;

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
}
