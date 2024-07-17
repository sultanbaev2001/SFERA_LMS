package sfera.service;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Lesson;
import sfera.entity.Module;
import sfera.entity.Task;
import sfera.entity.VideoFile;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqLesson;
import sfera.payload.TaskDto;
import sfera.payload.res.ResLesson;
import sfera.repository.LessonRepository;
import sfera.repository.ModuleRepository;
import sfera.repository.TaskRepository;
import sfera.repository.VideoFileRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final TaskRepository taskRepository;
    private final VideoFileRepository videoFileRepository;


    public ApiResponse saveLesson(ReqLesson lessonDTO){
        Module module = moduleRepository.findByOrderName(lessonDTO.getModuleName());
        boolean existsed = lessonRepository.existsByNameAndModuleNot(lessonDTO.getName(), module);
        if (!existsed) {
            for (String fileName : lessonDTO.getVideoFileName()) {
                List<VideoFile> allByFileName = videoFileRepository.findAllByFileName(fileName);

                List<Task> taskList = new ArrayList<>();
                for (TaskDto taskDto : lessonDTO.getTaskDtoList()) {
                    taskList.add(addTask(taskDto));
                }
                addLesson(lessonDTO, module, taskList, allByFileName);
                return new ApiResponse("Lesson successfully saved", HttpStatus.OK);
            }
        }
        return new ApiResponse("Lesson already exists", HttpStatus.CONFLICT);
    }


    public ApiResponse getAllLessons(){
        List<Lesson> lessons = lessonRepository.findAll();
        List<ResLesson> lessonDTOList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            ResLesson lessonDTO= ResLesson.builder()
                    .lessonId(lesson.getId())
                    .name(lesson.getName())
                    .moduleName(lesson.getModule().getOrderName())
                    .categoryName(lesson.getModule().getCategory().getName())
                    .build();
            lessonDTOList.add(lessonDTO);
        }
        return new ApiResponse("Success",HttpStatus.OK,lessonDTOList);
    }


    public ApiResponse getOneLesson(Integer id){
        List<String> videoFileName= new ArrayList<>();
        List<TaskDto> taskDTOList = new ArrayList<>();
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Lesson not found").statusCode(404).build());
        for (Task task : lesson.getTaskList()) {
            taskDTOList.add(getTaskDto(task));
        }
        for (VideoFile videoFile : lesson.getVideoFile()) {
            videoFileName.add(videoFile.getFileName());
        }
        ReqLesson reqLesson = ReqLesson.builder()
                .name(lesson.getName())
                .moduleName(lesson.getModule().getOrderName())
                .categoryName(lesson.getModule().getCategory().getName())
                .taskDtoList(taskDTOList)
                .videoFileName(videoFileName)
                .build();
        return new ApiResponse("Success", HttpStatus.OK, reqLesson);
    }


    public ApiResponse updateLesson(ReqLesson reqLesson){
        List<Task> taskList=new ArrayList<>();
        Module module = moduleRepository.findByOrderName(reqLesson.getModuleName());
        Lesson lesson = lessonRepository.findById(reqLesson.getId())
                .orElseThrow(() -> GenericException.builder().message("Lesson not found").statusCode(404).build());
        boolean existsed = lessonRepository.existsByNameAndModuleNot(reqLesson.getName(), lesson.getModule());
        if (!existsed) {
            for (TaskDto taskDto : reqLesson.getTaskDtoList()) {
                taskList.add(updateTask(taskDto));
            }
            lesson.setName(reqLesson.getName());
            lesson.setModule(module);
            lesson.setId(reqLesson.getId());
            lesson.setTaskList(taskList);
            lesson.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            lessonRepository.save(lesson);
            return new ApiResponse("Lesson successfully updated", HttpStatus.OK);
        }
        return new ApiResponse("Lesson already exists", HttpStatus.CONFLICT);
    }


    public ApiResponse deleteLesson(Integer id){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> GenericException.builder().message("Lesson not found").statusCode(404).build());
        taskRepository.deleteAll(lesson.getTaskList());
        lessonRepository.delete(lesson);
        return new ApiResponse("Lesson successfully deleted", HttpStatus.OK);
    }


    private Lesson addLesson(ReqLesson lessonDTO, Module module, List<Task> taskList, List<VideoFile> videoFileList){
        Lesson lesson=Lesson.builder()
                .name(lessonDTO.getName())
                .module(module)
                .taskList(taskList)
                .videoFile(videoFileList)
                .build();
        return lessonRepository.save(lesson);
    }

    private Task addTask(TaskDto taskDto){
        Task task= Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .build();
        return taskRepository.save(task);
    }


    private TaskDto getTaskDto(Task task){
        TaskDto taskDto= TaskDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .build();
        return taskDto;
    }

    private Task updateTask(TaskDto taskDto){
        Task task = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> GenericException.builder().message("Task not found").statusCode(404).build());
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        taskRepository.save(task);
        return task;
    }
}
