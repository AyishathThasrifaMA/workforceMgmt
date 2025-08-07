package com.thasrifa.workforcemgmt.workforcemgmt.model;

import lombok.Data;

@Data
public class TaskActivityHistory {
    private Long id;
    private Long taskId;
    private String activityMessage;
    private Long createdAt;
}
