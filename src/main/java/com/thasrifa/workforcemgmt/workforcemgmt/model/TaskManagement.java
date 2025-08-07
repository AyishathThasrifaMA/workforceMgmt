package com.thasrifa.workforcemgmt.workforcemgmt.model;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.ReferenceType;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Priority;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Task;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskManagement {
   private Long id;
   private Long referenceId;
   private ReferenceType referenceType;
   private Task task;
   private String description;
   private TaskStatus status;
   private Long assigneeId; // Simplified from Entity for this assignment
   private Long taskDeadlineTime;
   private Priority priority;
}


