package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Group;
import sfera.entity.Lesson;
import sfera.entity.LessonTracking;
import sfera.entity.User;
import sfera.exception.GenericException;
import sfera.mapper.LessonTrackingMapper;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqLessonTracking;
import sfera.payload.res.ResLessonTracking;
import sfera.repository.GroupRepository;
import sfera.repository.LessonRepository;
import sfera.repository.LessonTrackingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonTrackingService {

    private final LessonTrackingRepository lessonTrackingRepository;
    private final LessonTrackingMapper lessonTrackingMapper;
    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;


    public ApiResponse saveLessonTracking(ReqLessonTracking reqLessonTracking, User user) {
        boolean exists = lessonTrackingRepository.existsByGroupIdAndLessonAndActiveTrue(reqLessonTracking.getGroupId(), reqLessonTracking.getLessonId());
        if (!exists) {
            Group group = groupRepository.findById(reqLessonTracking.getGroupId()).orElseThrow(() -> GenericException.builder()
                    .message("LessonTracking not found")
                    .statusCode(404)
                    .build());
            if (group.getTeacher().equals(user)) {
                lessonTrackingRepository.save(lessonTrackingMapper.toLessonTracking(reqLessonTracking));
                return new ApiResponse("Success", true, HttpStatus.OK, null);
            }
            return new ApiResponse("Group does not belong to the teacher!", false, HttpStatus.BAD_REQUEST, null);
        }
        return new ApiResponse("LessonTracking already exsist", false, HttpStatus.BAD_REQUEST, null);
    }

    public ApiResponse getLessonTracking(Integer id, User user) {
        LessonTracking lessonTracking = lessonTrackingRepository.findById(id).orElseThrow(() -> GenericException.builder()
                .message("LessonTracking not found")
                .statusCode(404)
                .build());
        if (lessonTracking.getGroup().getTeacher().equals(user)){
            ResLessonTracking resLessonTracking = lessonTrackingMapper.toResLessonTracking(lessonTracking);
            return new ApiResponse("Success", true, HttpStatus.OK, resLessonTracking);
        }
        return new ApiResponse("LessonTracking does not belong to the teacher!", false, HttpStatus.BAD_REQUEST, null);
    }

    public ApiResponse getTeacherByLessonTrackingList(User user) {
        List<ResLessonTracking> resLessonTrackingList = new ArrayList<>();
        for (LessonTracking lessonTracking : lessonTrackingRepository.findAll()) {
            if (lessonTracking.getGroup().getTeacher().equals(user)) {
                ResLessonTracking resLessonTracking = lessonTrackingMapper.toResLessonTracking(lessonTracking);
                resLessonTrackingList.add(resLessonTracking);
                return new ApiResponse("Success", true, HttpStatus.OK, resLessonTrackingList);
            }
        }
        return new ApiResponse("Not success", true, HttpStatus.OK, null);
    }

    public ApiResponse updateLessonTracking(Integer id, ReqLessonTracking reqLessonTracking, User user) {
        LessonTracking lessonTrackingList = lessonTrackingRepository.findById(id).orElse(null);
        if (lessonTrackingList ==null) {
            Group group = groupRepository.findById(reqLessonTracking.getGroupId()).orElse(null);
            Lesson lesson = lessonRepository.findById(reqLessonTracking.getLessonId()).orElse(null);
            if (user == (group != null ? group.getTeacher() : null)){
                LessonTracking lessonTracking = new LessonTracking();
                lessonTracking.setId(id);
                lessonTracking.setGroup(group);
                lessonTracking.setLesson(lesson);
                lessonTracking.setActive(reqLessonTracking.isActive());
                return new ApiResponse("Success", true, HttpStatus.OK, null);
            }
            return new ApiResponse("Group does not belong to the teacher!", false, HttpStatus.BAD_REQUEST, null);
        }
        return new ApiResponse("LessonTracking not found", false, HttpStatus.BAD_REQUEST, null);
    }

    public ApiResponse deleteLessonTracking(Integer id) {
        lessonTrackingRepository.deleteById(id);
        return new ApiResponse("Success", true, HttpStatus.OK, null);
    }


    public void updateLesTrac(Integer id, ReqLessonTracking reqLessonTracking){

    }
}
