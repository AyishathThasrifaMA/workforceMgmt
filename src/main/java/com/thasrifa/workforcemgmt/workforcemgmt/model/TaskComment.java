package com.thasrifa.workforcemgmt.workforcemgmt.model;
import lombok.Data;

@Data
public class TaskComment {
    private Long id;
    private Long taskId;
    private Long userId;
    private String commentText;
    private Long createdAt; // Unix timestamp
}
