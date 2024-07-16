package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.entity.User;
import sfera.repository.HomeWorkRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeWorkService {


    private final HomeWorkRepository homeWorkRepository;


//    Vaqt boyicha Studentga tegishli barcha homeworklarga berilgan ballni umumiy yig'indisini olib beradi

    public Integer getTotalScoreByStudentsAndCurrentMonth(User student) {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1); // Bu oyning birinchi kuni
        LocalDate endDate = LocalDate.now(); // Hozirgi vaqt
        return homeWorkRepository.findTotalScoreByStudentsAndPeriod(student, startDate, endDate);
    }


}
