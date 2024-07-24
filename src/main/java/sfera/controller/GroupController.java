package sfera.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.payload.ApiResponse;
import sfera.payload.req.ReqGroup;
import sfera.service.GroupService;

@RestController
@RequestMapping("group/")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Groupni qoshish")
    @PostMapping("save/")
    public ResponseEntity<ApiResponse> save(@RequestBody ReqGroup reqGroup){
        ApiResponse apiResponse = groupService.saveGroup(reqGroup);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Groupni hammasini get qilish")
    @GetMapping("list/")
    public ResponseEntity<ApiResponse> list(){
        ApiResponse apiResponse = groupService.getAllGroup();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Groupni update qilish")
    @PutMapping("update/{groupId}")
    public ResponseEntity<ApiResponse> update(@PathVariable("groupId") int groupId, @RequestBody ReqGroup reqGroup){
        ApiResponse apiResponse = groupService.editGroup(groupId, reqGroup);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "ADMIN  Groupni activeni uzgartirish ")
    @PutMapping("deActive/{groupId}")
    public ResponseEntity<ApiResponse> deActive(@PathVariable("groupId") int groupId,@RequestParam boolean active){
        ApiResponse apiResponse = groupService.deActivateGroup(groupId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @Operation(summary = "Admin groupni delete qilish uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") int id){
        ApiResponse apiResponse = groupService.deleteGroup(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    @Operation(summary = "ADMIN student qo'shguncha categoryasini tanlaganda shu categoryga tegishli grouplar uchun")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_TEACHER')")
    @GetMapping("categoryByGroups/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryByGroups(@PathVariable("categoryId") int categoryId){
        ApiResponse apiResponse = groupService.getCategoryByGroups(categoryId);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}
