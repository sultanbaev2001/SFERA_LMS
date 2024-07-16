package sfera.controller;

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
    @PostMapping("save/")
    public ResponseEntity<ApiResponse> save(@RequestBody ReqGroup reqGroup){
        ApiResponse apiResponse = groupService.saveGroup(reqGroup);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("list/")
    public ResponseEntity<ApiResponse> list(){
        ApiResponse apiResponse = groupService.getAllGroup();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PutMapping("update/{groupId}")
    public ResponseEntity<ApiResponse> update(@PathVariable("groupId") int groupId, @RequestBody ReqGroup reqGroup){
        ApiResponse apiResponse = groupService.editGroup(groupId, reqGroup);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    @PutMapping("deActive/{groupId}")
    public ResponseEntity<ApiResponse> deActive(@PathVariable("groupId") int groupId,@RequestParam boolean active){
        ApiResponse apiResponse = groupService.deActivateGroup(groupId, active);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
