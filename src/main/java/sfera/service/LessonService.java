package sfera.service;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.*;
import sfera.entity.Module;
import sfera.exception.GenericException;
import sfera.payload.ApiResponse;
import sfera.payload.ResLessonDTO;
import sfera.payload.req.ReqLesson;
import sfera.payload.TaskDto;
import sfera.payload.res.ResLesson;
import sfera.payload.res.ResLessonAdmin;
import sfera.payload.res.ResLessons;
import sfera.payload.res.ResModuleByLesson;
import sfera.repository.FileRepository;
import sfera.repository.LessonRepository;
import sfera.repository.ModuleRepository;
import sfera.repository.TaskRepository;

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
    private final FileRepository fileRepository;


    public ApiResponse saveLesson(ReqLesson lessonDTO,User teacher){

        Module module = moduleRepository.findById(lessonDTO.getModuleId())
                .orElseThrow(() -> GenericException.builder().message("Module not found").statusCode(404).build());
        if (!module.getTeacher().getId().equals(teacher.getId())) {
            return new ApiResponse("Siz bu modulga lesson qusholmaysiz",HttpStatus.BAD_REQUEST);
        }
        List<Task> taskList = new ArrayList<>();
        boolean existsed = lessonRepository.existsByNameAndModuleNot(lessonDTO.getName(), module);
        if (!existsed) {

            if (lessonDTO.getFileIds().isEmpty()){
                for (TaskDto taskDto : lessonDTO.getTaskDtoList()) {
                    taskList.add(addTasks(taskDto));
                }
                addLessons(lessonDTO,module,taskList);
                return new ApiResponse("Lesson successfully saved ", HttpStatus.OK,null);

            }else {
                for (Long id : lessonDTO.getFileIds()) {
                    List<File> allByFileName = fileRepository.findAllById(id);
                    File file = fileRepository.findById(id)
                            .orElseThrow(() -> GenericException.builder().message("Video file not found").statusCode(404).build());
                    for (TaskDto taskDto : lessonDTO.getTaskDtoList()) {
                        taskList.add(addTask(taskDto,file));
                    }
                    addLesson(lessonDTO, module, taskList, allByFileName);
                }
                    return new ApiResponse("Lesson successfully saved", HttpStatus.OK,null);
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



    public ApiResponse getAllLessonByTeacher(User teacher){
        List<Module> allByTeacherId = moduleRepository.findAllByTeacherId(teacher.getId());
        List<ResLesson> lessonDTOList = new ArrayList<>();
        for (Module module : allByTeacherId) {
            List<Lesson> allByModuleId = lessonRepository.findAllByModule_Id(module.getId());
            for (Lesson lesson : allByModuleId) {
                ResLesson lessonDTO= ResLesson.builder()
                        .lessonId(lesson.getId())
                        .name(lesson.getName())
                        .moduleName(module.getOrderName())
                        .categoryName(module.getCategory().getName())
                        .build();
                lessonDTOList.add(lessonDTO);
            }
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
        for (File file : lesson.getFiles()) {
            videoFileName.add(file.getFileName());
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


    private Lesson addLesson(ReqLesson lessonDTO, Module module, List<Task> taskList, List<File> files){
        Lesson lesson=Lesson.builder()
                .name(lessonDTO.getName())
                .module(module)
                .taskList(taskList)
                .files(files)
                .build();
        return lessonRepository.save(lesson);
    }

    private Lesson addLessons(ReqLesson lessonDTO, Module module, List<Task> taskList){
        Lesson lesson=Lesson.builder()
                .name(lessonDTO.getName())
                .module(module)
                .taskList(taskList)
                .build();
        return lessonRepository.save(lesson);
    }

    public Task addTask(TaskDto taskDto, File file){
        Task task= Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .file(file)
                .build();
        return taskRepository.save(task);
    }



    public Task addTasks(TaskDto taskDto){
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
