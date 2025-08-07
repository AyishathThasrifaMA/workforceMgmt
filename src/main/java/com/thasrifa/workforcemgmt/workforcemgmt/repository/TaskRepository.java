package com.thasrifa.workforcemgmt.workforcemgmt.repository;


import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskManagement;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Priority;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.TaskStatus;


import java.util.List;
import java.util.Optional;


public interface TaskRepository {
   Optional<TaskManagement> findById(Long id);
   TaskManagement save(TaskManagement task);
   List<TaskManagement> findAll();
   List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, com.thasrifa.workforcemgmt.workforcemgmt.model.enums.ReferenceType referenceType);
   List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
   List<TaskManagement> findByPriority(Priority priority);
   
}




