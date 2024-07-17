package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.entity.Group;
import sfera.entity.HomeWork;
import sfera.entity.Lesson;
import sfera.entity.User;
import sfera.repository.HomeWorkRepository;
import sfera.repository.LessonRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeWorkService {


    private final HomeWorkRepository homeWorkRepository;
    private final LessonRepository lessonRepository;


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



    public Integer getHomeworksByStudentAndPreviousMonth(User student) {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1); // Oldingi oyning birinchi kuni
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1); // Joriy oyning birinchi kuni oldin
        return homeWorkRepository.findTotalScoreByStudentsAndPeriod(student, startDate, endDate);
    }


}
