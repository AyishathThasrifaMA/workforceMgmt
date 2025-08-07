package com.thasrifa.workforcemgmt.workforcemgmt.service;


import com.thasrifa.workforcemgmt.workforcemgmt.dto.*;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Priority;

import java.time.LocalDate;
import java.util.List;


public interface TaskManagementService {
   List<TaskManagementDto> createTasks(TaskCreateRequest request);
   List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
   String assignByReference(AssignByReferenceRequest request);
   List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
   TaskManagementDto findTaskById(Long id);
   List<TaskManagementDto> fetchSmartDailyTasks(TaskFetchByDateRequest request);
   void updateTaskPriority(Long taskId, Priority newPriority);
   List<TaskManagementDto> getTasksByPriority(Priority priority);
   TaskCommentDto addCommentToTask(TaskCommentRequestDto requestDto);

   //for get all 
   List<TaskManagementDto> getAllTasks();
  

}


