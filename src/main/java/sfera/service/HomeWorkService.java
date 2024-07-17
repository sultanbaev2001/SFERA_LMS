package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.entity.HomeWork;
import sfera.entity.Lesson;
import sfera.entity.User;
import sfera.exception.GenericException;
import sfera.repository.HomeWorkRepository;
import sfera.repository.LessonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HomeWorkService {


    private final HomeWorkRepository homeWorkRepository;
    private final LessonRepository lessonRepository;


//    Vaqt boyicha Studentga tegishli barcha homeworklarga berilgan ballni umumiy yig'indisini olib beradi

    public Integer getTotalScoreByStudentsAndCurrentMonth(User student) {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1); // Bu oyning birinchi kuni
        LocalDate endDate = LocalDate.now(); // Hozirgi vaqt
        return homeWorkRepository.findTotalScoreByStudentsAndPeriod(student, startDate, endDate);
    }

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
        return (double) score/sum;

    }


}
