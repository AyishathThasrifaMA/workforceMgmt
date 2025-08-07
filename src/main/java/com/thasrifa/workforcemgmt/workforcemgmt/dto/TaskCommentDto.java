package com.thasrifa.workforcemgmt.workforcemgmt.dto;
import lombok.Data;

@Data
public class TaskCommentDto {
    private Long userId;
    private String commentText;
    private Long createdAt;
}
