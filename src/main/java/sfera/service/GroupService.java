package sfera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sfera.entity.Category;
import sfera.entity.Group;
import sfera.entity.User;
import sfera.exception.GenericException;
import sfera.exception.UserNotFoundException;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqGroup;
import sfera.payload.res.ResGroup;
import sfera.repository.CategoryRepository;
import sfera.repository.GroupRepository;
import sfera.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ApiResponse saveGroup(ReqGroup reqGroup){
        boolean exists = groupRepository.existsByName(reqGroup.getName());
        Category category = categoryRepository.findById(reqGroup.getCategoryId()).orElseThrow(() -> GenericException.builder()
                .message("Category not found").statusCode(404).build());
        User teacher = userRepository.findById(reqGroup.getTeacherId()).orElseThrow(UserNotFoundException::new);
        if(exists){
            return new ApiResponse("Group name already exits",false, HttpStatus.BAD_REQUEST,null);
        }
        Group group=Group.builder()
                .name(reqGroup.getName())
                .category(category)
                .days(reqGroup.getDays())
                .teacher(teacher)
                .active(true)
                .startDate(LocalDate.now())
                .startTime(reqGroup.getStartTime())
                .endTime(reqGroup.getEndTime())
                .build();
        groupRepository.save(group);
        return new ApiResponse("Group added",true, HttpStatus.CREATED,null);
    }

    public ApiResponse getAllGroup(){
        List<Group> groups = groupRepository.findAll();
        List<ResGroup> resGroups=new ArrayList<>();
        if (groups.isEmpty()){
            return new ApiResponse("No groups found",false,HttpStatus.NOT_FOUND,null);
        }
        for (Group group : groups){
            ResGroup resGroup=ResGroup.builder()
                    .name(group.getName())
                    .categoryName(group.getCategory().getName())
                    .teacherName(group.getTeacher().getFirstname()+" "+group.getTeacher().getLastname())
                    .active(group.isActive())
                    .build();
            resGroups.add(resGroup);
        }
        return new ApiResponse("Groups found",true,HttpStatus.OK,resGroups);
    }

    public ApiResponse editGroup(int groupId,ReqGroup reqGroup){
        boolean exists = groupRepository.existsByNameAndIdNot(reqGroup.getName(),groupId);
        Category category = categoryRepository.findById(reqGroup.getCategoryId()).orElseThrow(() -> GenericException.builder()
                .message("Category not found").statusCode(404).build());
        Group group = groupRepository.findById(groupId).orElseThrow(() -> GenericException.builder().
                message("Group not found").statusCode(404).build());
        User teacher = userRepository.findById(reqGroup.getTeacherId()).orElseThrow(UserNotFoundException::new);
        if(exists){
            return new ApiResponse("Group name already exits",false, HttpStatus.BAD_REQUEST,null);
        }
        group.setName(reqGroup.getName());
        group.setCategory(category);
        group.setDays(reqGroup.getDays());
        group.setStartDate(LocalDate.now());
        group.setStartTime(reqGroup.getStartTime());
        group.setEndTime(reqGroup.getEndTime());
        group.setTeacher(teacher);
        groupRepository.save(group);
        return new ApiResponse("Group updated",true, HttpStatus.OK,group);
    }

    public ApiResponse deActivateGroup(int groupId,boolean active){
        Group group = groupRepository.findById(groupId).orElseThrow(() -> GenericException.builder()
                .message("Group not found").statusCode(404).build());
        group.setActive(active);
        groupRepository.save(group);
        return new ApiResponse("Group deactivated",true, HttpStatus.OK,group);
    }
}
