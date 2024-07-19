package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.entity.HomeWork;
import sfera.entity.User;
import sfera.payload.ApiResponse;
import sfera.repository.HomeWorkRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final HomeWorkRepository homeWorkRepository;


    public ApiResponse getStudentHomework(User teacher){

        homeWorkRepository.findAllHomeworksByTeacher(teacher)


        return null;
    }
}
