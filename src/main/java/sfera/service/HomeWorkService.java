package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.HomeWork;
import sfera.entity.Lesson;
import sfera.entity.Group;
import sfera.entity.HomeWork;
import sfera.entity.Lesson;
import sfera.entity.User;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.repository.HomeWorkRepository;
import sfera.repository.LessonRepository;
import sfera.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeWorkService {


    private final HomeWorkRepository homeWorkRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;


    LocalDate startDate = LocalDate.now().withDayOfMonth(1); // Bu oyning birinchi kuni
    LocalDate endDate = LocalDate.now(); // Hozirgi vaqt

//    Shu oy bo'yicha Studentga tegishli barcha homeworklarga berilgan ballni umumiy yig'indisini olib beradi!

    public Integer getTotalScoreByStudentsAndCurrentMonth(User student) {
        return homeWorkRepository.findTotalScoreByStudentsAndPeriod(student, startDate, endDate);
    }


    //    Shu oy bo'yicha guruhdagi barcha student to'plagan umumiy ballar yig'indisini olib beradi!
    public Integer getTotalScoreByGroupAndCurrentMonth(Group group) {

        return homeWorkRepository.findTotalScoreByGroupAndPeriod(group, startDate, endDate);
    }


//    O'tgan oyning lessonlar boyicha student toplashi kerak bolgan umumiy ballar va homeworklar boyicha
//    to'plagan ballarini nisbatini olib beradi -> allHomeworksScore/allLessonsScore
    public double getHomeworksByStudentPercentageOfMonth(User student) {
        int sum=0; Object checkLesson=null;
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1); // Oldingi oyning birinchi kuni
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1); // Joriy oyning birinchi kuni oldin
        Integer score = homeWorkRepository.findTotalScoreByStudentsAndPeriod(student, startDate, endDate);
        List<HomeWork> homeWorkList = homeWorkRepository.findAllByDueDateBetweenAndStudent(startDate, endDate, student);
        for (HomeWork homeWork : homeWorkList) {
            Lesson lesson = lessonRepository.findLessonByTaskList(List.of(homeWork.getTask())).orElseThrow(() -> GenericException.builder()
                    .message("Lesson Not Found").statusCode(404).build());
            if (!Objects.equals(checkLesson, lesson)){
                sum+=lesson.getTaskList().size()*10;
                checkLesson=lesson;
            }
        }
        return score == null ? 0 : (double) score/sum;
    }

    public ApiResponse updateHomeworkScore(Long studentId, Integer homeworkId, Integer inScore){
//        boolean updated = homeWorkRepository.updateHomeWorkByScore(studentId, homeworkId, inScore);
        HomeWork homeWork = homeWorkRepository.findById(homeworkId).orElseThrow(() -> GenericException.builder()
                .message("Homework Not Found")
                .statusCode(404)
                .build());
        User user = userRepository.findById(studentId).orElseThrow(() -> GenericException.builder()
                .message("Student Not Found")
                .statusCode(404)
                .build());
        if (homeWork.getStudent().getId().equals(user.getId())){
            homeWork.setScore(inScore);
            homeWorkRepository.save(homeWork);
            return new ApiResponse("Success", HttpStatus.OK, null);
        }
        return new ApiResponse("Not success",HttpStatus.OK, null);
    }


}
