package com.thasrifa.workforcemgmt.workforcemgmt.dto;


import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.ReferenceType;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Priority;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Task;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.TaskStatus;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
   private Long id;
   private Long referenceId;
   private ReferenceType referenceType;
   private Task task;
   private String description;
   private TaskStatus status;
   private Long assigneeId;
   private Long taskDeadlineTime;
   private Priority priority;
   private List<TaskCommentDto> comments;
   private List<TaskActivityHistoryDto> activityHistory;

}
