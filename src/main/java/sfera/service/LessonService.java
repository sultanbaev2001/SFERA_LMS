package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Lesson;
import sfera.entity.Module;
import sfera.entity.Task;
import sfera.entity.VideoFile;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.ResLessonDTO;
import sfera.payload.req.ReqLesson;
import sfera.payload.TaskDto;
import sfera.payload.res.ResLesson;
import sfera.payload.res.ResLessonAdmin;
import sfera.payload.res.ResLessons;
import sfera.payload.res.ResModuleByLesson;
import sfera.repository.LessonRepository;
import sfera.repository.ModuleRepository;
import sfera.repository.TaskRepository;
import sfera.repository.VideoFileRepository;

import java.sql.Timestamp;
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
        Module module = moduleRepository.findById(lessonDTO.getModuleId())
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
        boolean existsed = lessonRepository.existsByNameAndModuleNot(lessonDTO.getName(), module);
        if (!existsed) {
            for (String fileName : lessonDTO.getVideoFileName()) {
                List<VideoFile> allByFileName = videoFileRepository.findAllByFileName(fileName);
                VideoFile file = videoFileRepository.findByFileName(fileName)
                        .orElseThrow(() -> GenericException.builder().message("Video file not found").statusCode(404).build());
                List<Task> taskList = new ArrayList<>();
                for (TaskDto taskDto : lessonDTO.getTaskDtoList()) {
                    taskList.add(addTask(taskDto,allByFileName));
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
        Module module = moduleRepository.findById(lesson.getModule().getId())
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
        for (Task task : lesson.getTaskList()) {
            taskDTOList.add(getTaskDto(task));
        }
        for (VideoFile videoFile : lesson.getVideoFile()) {
            videoFileName.add(videoFile.getFileName());
        }
        ResLessonDTO reqLesson = ResLessonDTO.builder()
                .name(lesson.getName())
                .moduleName(module.getOrderName())
                .categoryName(lesson.getModule().getCategory().getName())
                .taskDtoList(taskDTOList)
                .videoFileName(videoFileName)
                .build();
        return new ApiResponse("Success", HttpStatus.OK, reqLesson);
    }


    public ApiResponse updateLesson(ReqLesson reqLesson){
        List<Task> taskList=new ArrayList<>();
        Module module = moduleRepository.findById(reqLesson.getModuleId())
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
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

    public ApiResponse getModuleByLesson(){
        List<ResLessonAdmin> resLessonAdminList=new ArrayList<>();
        List<Module> modules = moduleRepository.findAll();
        if (modules.isEmpty()){
            return new ApiResponse("Module not found", HttpStatus.NOT_FOUND);
        }
        for (Module module : modules) {
            Integer lessonCount = lessonRepository.countByModule(module);
            ResLessonAdmin resLessonAdmin=ResLessonAdmin.builder()
                    .moduleId(module.getId())
                    .moduleName(module.getOrderName())
                    .lessonCount(lessonCount)
                    .build();
            resLessonAdminList.add(resLessonAdmin);
        }
        return new ApiResponse("Success",HttpStatus.OK,resLessonAdminList);
    }

    public ApiResponse getLessons(int moduleId){
        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> GenericException.builder()
                .message("Module not found").statusCode(404).build());
        List<Lesson> lessons = lessonRepository.findByModule(module);
        if (lessons.isEmpty()){
            return new ApiResponse("Lesson not found", HttpStatus.NOT_FOUND);
        }
        List<ResLessons> resLessonsList=new ArrayList<>();
        for (Lesson lesson : lessons) {
            ResLessons resLessons=ResLessons.builder()
                    .lessonId(lesson.getId())
                    .lessonName(lesson.getName())
                    .build();
            resLessonsList.add(resLessons);
        }
        ResModuleByLesson resModuleByLesson=ResModuleByLesson.builder()
                .moduleName(module.getOrderName())
                .resLessonList(resLessonsList).build();
        return new ApiResponse("Success",HttpStatus.OK,resModuleByLesson);
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

    public Task addTask(TaskDto taskDto,List<VideoFile> videoFile){
        Task task= Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .files(videoFile)
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
