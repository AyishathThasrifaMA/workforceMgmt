package com.thasrifa.workforcemgmt.workforcemgmt.repository;

import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskComment;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskCommentRepository implements TaskCommentRepository {

    private final Map<Long, TaskComment> commentStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public List<TaskComment> findByTaskIdOrderByCreatedAtAsc(Long taskId) {
        return commentStore.values().stream()
                .filter(comment -> comment.getTaskId().equals(taskId))
                .sorted(Comparator.comparing(TaskComment::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskComment> findById(Long id) {
        return Optional.ofNullable(commentStore.get(id));
    }

    @Override
    public TaskComment save(TaskComment comment) {
        if (comment.getId() == null) {
            comment.setId(idCounter.incrementAndGet());
        }
        commentStore.put(comment.getId(), comment);
        return comment;
    }
}