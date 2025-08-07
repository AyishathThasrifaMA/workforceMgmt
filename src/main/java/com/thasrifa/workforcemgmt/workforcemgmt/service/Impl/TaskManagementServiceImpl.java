package com.thasrifa.workforcemgmt.workforcemgmt.service.Impl;
import com.thasrifa.workforcemgmt.workforcemgmt.exception.ResourceNotFoundException;
import com.thasrifa.workforcemgmt.workforcemgmt.dto.*;
import com.thasrifa.workforcemgmt.workforcemgmt.mapper.ITaskManagementMapper;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskActivityHistory;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskComment;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskManagement;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.*;
import com.thasrifa.workforcemgmt.workforcemgmt.repository.TaskActivityHistoryRepository;
import com.thasrifa.workforcemgmt.workforcemgmt.repository.TaskCommentRepository;
import com.thasrifa.workforcemgmt.workforcemgmt.repository.TaskRepository;
import com.thasrifa.workforcemgmt.workforcemgmt.service.TaskManagementService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class TaskManagementServiceImpl implements TaskManagementService {
   private final TaskRepository taskRepository;
   private final ITaskManagementMapper taskMapper;
   private final TaskCommentRepository taskCommentRepository;
   private final TaskActivityHistoryRepository taskActivityHistoryRepository;

   public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper, TaskCommentRepository taskCommentRepository,
                                     TaskActivityHistoryRepository taskActivityHistoryRepository) {
       this.taskRepository = taskRepository;
       this.taskMapper = taskMapper;
       this.taskCommentRepository = taskCommentRepository;
       this.taskActivityHistoryRepository = taskActivityHistoryRepository;
   }

   @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        TaskManagementDto dto = taskMapper.modelToDto(task);
        List<TaskComment> comments = taskCommentRepository.findByTaskIdOrderByCreatedAtAsc(id);
        dto.setComments(taskMapper.commentModelListToDtoList(comments));
        List<TaskActivityHistory> activityHistory = taskActivityHistoryRepository.findByTaskIdOrderByCreatedAtAsc(id);
        dto.setActivityHistory(taskMapper.activityModelListToDtoList(activityHistory));
        return dto;
    }

   @Override
   public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
       List<TaskManagement> createdTasks = new ArrayList<>();
       for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
           TaskManagement newTask = new TaskManagement();
           newTask.setReferenceId(item.getReferenceId());
           newTask.setReferenceType(item.getReferenceType());
           newTask.setTask(item.getTask());
           newTask.setAssigneeId(item.getAssigneeId());
           newTask.setPriority(item.getPriority());
           newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
           newTask.setStatus(TaskStatus.ASSIGNED);
           newTask.setDescription("New task created.");
           createdTasks.add(taskRepository.save(newTask));
       }
       return taskMapper.modelListToDtoList(createdTasks);
   }


   @Override
   public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
       List<TaskManagement> updatedTasks = new ArrayList<>();
       for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
           TaskManagement task = taskRepository.findById(item.getTaskId())
                   .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));
           if (item.getTaskStatus() != null) {
               task.setStatus(item.getTaskStatus());
           }
           if (item.getDescription() != null) {
               task.setDescription(item.getDescription());
           }
           updatedTasks.add(taskRepository.save(task));
       }
       return taskMapper.modelListToDtoList(updatedTasks);
   }
   
    // get all 
    @Override
    public List<TaskManagementDto> getAllTasks() {
        List<TaskManagement> tasks = taskRepository.findAll();
        return taskMapper.modelListToDtoList(tasks);
    }


//    @Override
//    public String assignByReference(AssignByReferenceRequest request) {
//        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
//        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());
//        for (Task taskType : applicableTasks) {
//            List<TaskManagement> tasksOfType = existingTasks.stream()
//                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
//                    .collect(Collectors.toList());
//            // BUG #1 is here. It should assign one and cancel the rest.
//            // Instead, it reassigns ALL of them.
//            if (!tasksOfType.isEmpty()) {
//                for (TaskManagement taskToUpdate : tasksOfType) {
//                    taskToUpdate.setAssigneeId(request.getAssigneeId());
//                    taskRepository.save(taskToUpdate);
//                }
//            } else {
//                // Create a new task if none exist
//                TaskManagement newTask = new TaskManagement();
//                newTask.setReferenceId(request.getReferenceId());
//                newTask.setReferenceType(request.getReferenceType());
//                newTask.setTask(taskType);
//                newTask.setAssigneeId(request.getAssigneeId());
//                newTask.setStatus(TaskStatus.ASSIGNED);
//                taskRepository.save(newTask);
//            }
//        }
//        return "Tasks assigned successfully for reference " + request.getReferenceId();
//    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());
        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());
            for (TaskManagement oldTask : tasksOfType) {
                oldTask.setStatus(TaskStatus.CANCELLED);
                oldTask.setDescription("Cancelled due to reassignment.");
                taskRepository.save(oldTask);
            }
        
            TaskManagement newTask = new TaskManagement();
            newTask.setId(null); // Force Hibernate to treat it as a new entity
            newTask.setReferenceId(request.getReferenceId());
            newTask.setReferenceType(request.getReferenceType());
            newTask.setTask(taskType);
            newTask.setAssigneeId(request.getAssigneeId());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("Reassigned task.");
            newTask.setPriority(Priority.MEDIUM); // default or choose appropriately
            newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000); // +1 day or use your logic
            taskRepository.save(newTask);
        }
        return "Tasks reassigned successfully for reference " + request.getReferenceId();
    }

//    @Override
//    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
//        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());
//        // BUG #2 is here. It should filter out CANCELLED tasks but doesn't.
//        List<TaskManagement> filteredTasks = tasks.stream()
//                .filter(task -> {
//                    // This logic is incomplete for the assignment.
//                    // It should check against startDate and endDate.
//                    // For now, it just returns all tasks for the assignees.
//                    return true;
//                })
//                .collect(Collectors.toList());
//        return taskMapper.modelListToDtoList(filteredTasks);
//    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());
        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> {
                    Long deadline = task.getTaskDeadlineTime();
                    boolean inDateRange = deadline >= request.getStartDate() && deadline <= request.getEndDate();
                    boolean notCancelled = task.getStatus() != TaskStatus.CANCELLED;
                    return inDateRange && notCancelled;
                })
                .collect(Collectors.toList());
        return taskMapper.modelListToDtoList(filteredTasks);
    }

    // feature 1 smart
    @Override
    public List<TaskManagementDto> fetchSmartDailyTasks(TaskFetchByDateRequest request) {
        long start = request.getStartDate();
        long end = request.getEndDate();
        List<TaskManagement> tasks = taskRepository.findAll();
        List<TaskManagement> smartFilteredTasks = tasks.stream()
                .filter(task -> isTaskActive(task.getStatus()))
                .filter(task ->
                        (task.getTaskDeadlineTime() >= start && task.getTaskDeadlineTime() <= end) || // within range
                        (task.getTaskDeadlineTime() < start) // started before but still open
                )
                .collect(Collectors.toList());
        return taskMapper.modelListToDtoList(smartFilteredTasks);
    }
    private boolean isTaskActive(TaskStatus status) {
        return status != null && status != TaskStatus.CANCELLED && status != TaskStatus.COMPLETED;
    }

    //feature 2 implement task priority
    @Override
    public void updateTaskPriority(Long taskId, Priority newPriority) {
        TaskManagement task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        task.setPriority(newPriority);
        taskRepository.save(task);
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        List<TaskManagement> tasks = taskRepository.findByPriority(priority);
        return taskMapper.modelListToDtoList(tasks);
    }

    //feature3
     @Override
    public TaskCommentDto addCommentToTask(TaskCommentRequestDto requestDto) {
        TaskManagement task = taskRepository.findById(requestDto.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + requestDto.getTaskId()));
        TaskComment comment = new TaskComment();
        comment.setTaskId(requestDto.getTaskId());
        comment.setUserId(requestDto.getUserId());
        comment.setCommentText(requestDto.getCommentText());
        comment.setCreatedAt(System.currentTimeMillis());
        TaskComment savedComment = taskCommentRepository.save(comment);
        logActivity(task.getId(), String.format("User %d added a comment.", requestDto.getUserId()));
        return taskMapper.commentModelToDto(savedComment);
    }
    private void logActivity(Long taskId, String message) {
        TaskActivityHistory activity = new TaskActivityHistory();
        activity.setTaskId(taskId);
        activity.setActivityMessage(message);
        activity.setCreatedAt(System.currentTimeMillis());
        taskActivityHistoryRepository.save(activity);
    }
}
