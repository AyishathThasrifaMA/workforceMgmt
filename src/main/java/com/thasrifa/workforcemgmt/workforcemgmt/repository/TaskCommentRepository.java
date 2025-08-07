package com.thasrifa.workforcemgmt.workforcemgmt.repository;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskComment;
import java.util.List;
import java.util.Optional;

public interface TaskCommentRepository {
    List<TaskComment> findByTaskIdOrderByCreatedAtAsc(Long taskId);
    Optional<TaskComment> findById(Long id);
    TaskComment save(TaskComment comment);
}