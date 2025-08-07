package com.thasrifa.workforcemgmt.workforcemgmt.controller;


import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Priority;
import com.thasrifa.workforcemgmt.workforcemgmt.model.response.Response;
import com.thasrifa.workforcemgmt.workforcemgmt.dto.*;
import com.thasrifa.workforcemgmt.workforcemgmt.service.TaskManagementService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {


   private final TaskManagementService taskManagementService;


   public TaskManagementController(TaskManagementService taskManagementService) {
       this.taskManagementService = taskManagementService;
   }


   @GetMapping("/{id}")
   public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
       return new Response<>(taskManagementService.findTaskById(id));
   }


   @PostMapping("/create")
   public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
       return new Response<>(taskManagementService.createTasks(request));
   }


   @PostMapping("/update")
   public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
       return new Response<>(taskManagementService.updateTasks(request));
   }


   @PostMapping("/assign-by-ref")
   public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
       return new Response<>(taskManagementService.assignByReference(request));
   }


   @PostMapping("/fetch-by-date/v2")
   public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
       return new Response<>(taskManagementService.fetchTasksByDate(request));
   }

   //added get for checking all the available tasks
    @GetMapping("/all")
    public Response<List<TaskManagementDto>> getAllTasks() {
        return new Response<>(taskManagementService.getAllTasks());
    }

    //feature 1 a smart daily task view
    @PostMapping("/smart-fetch-by-date")
    public ResponseEntity<ResponseDto<List<TaskManagementDto>>> fetchSmartDailyTasks(
            @RequestBody TaskFetchByDateRequest request) {

        List<TaskManagementDto> data = taskManagementService.fetchSmartDailyTasks(request);

        ResponseDto<List<TaskManagementDto>> response = ResponseDto.<List<TaskManagementDto>>builder()
                .code(200)
                .message("Smart daily task fetch successful")
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }
    //feature 2
    //  Update task priority
    @PutMapping("/update-priority")
    public ResponseEntity<ResponseDto<Void>> updateTaskPriority(@RequestBody TaskPriorityUpdateRequest request) {
        taskManagementService.updateTaskPriority(request.getTaskId(), request.getPriority());
        return ResponseEntity.ok(new ResponseDto<>(200, "Priority updated successfully", null));
    }

    // Fetch tasks by priority
    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<ResponseDto<List<TaskManagementDto>>> getTasksByPriority(@PathVariable Priority priority) {
        List<TaskManagementDto> tasks = taskManagementService.getTasksByPriority(priority);
        return ResponseEntity.ok(new ResponseDto<>(200, "Tasks fetched successfully", tasks));
    }

    //feature 3
    @PostMapping("/comment")
    public ResponseEntity<ResponseDto<TaskCommentDto>> addComment(@RequestBody TaskCommentRequestDto requestDto) {
        TaskCommentDto commentDto = taskManagementService.addCommentToTask(requestDto);
        ResponseDto<TaskCommentDto> response = new ResponseDto<>(200, "Comment added successfully", commentDto);
        return ResponseEntity.ok(response);
    }




}
