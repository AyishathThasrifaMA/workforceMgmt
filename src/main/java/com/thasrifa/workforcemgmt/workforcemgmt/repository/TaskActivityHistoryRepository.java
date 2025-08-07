package com.thasrifa.workforcemgmt.workforcemgmt.repository;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskActivityHistory;
import java.util.List;
import java.util.Optional;

public interface TaskActivityHistoryRepository {
    List<TaskActivityHistory> findByTaskIdOrderByCreatedAtAsc(Long taskId);
    Optional<TaskActivityHistory> findById(Long id);
    TaskActivityHistory save(TaskActivityHistory activity);
}