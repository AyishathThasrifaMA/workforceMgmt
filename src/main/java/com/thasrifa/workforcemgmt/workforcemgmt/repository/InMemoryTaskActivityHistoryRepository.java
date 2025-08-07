package com.thasrifa.workforcemgmt.workforcemgmt.repository;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskActivityHistory;
import org.springframework.stereotype.Repository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskActivityHistoryRepository implements TaskActivityHistoryRepository {
    private final Map<Long, TaskActivityHistory> activityStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public List<TaskActivityHistory> findByTaskIdOrderByCreatedAtAsc(Long taskId) {
        return activityStore.values().stream()
                .filter(history -> history.getTaskId().equals(taskId))
                .sorted(Comparator.comparing(TaskActivityHistory::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskActivityHistory> findById(Long id) {
        return Optional.ofNullable(activityStore.get(id));
    }

    @Override
    public TaskActivityHistory save(TaskActivityHistory activity) {
        if (activity.getId() == null) {
            activity.setId(idCounter.incrementAndGet());
        }
        activityStore.put(activity.getId(), activity);
        return activity;
    }
}