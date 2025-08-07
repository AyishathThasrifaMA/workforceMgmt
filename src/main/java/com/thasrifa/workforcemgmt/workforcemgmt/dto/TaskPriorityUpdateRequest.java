package com.thasrifa.workforcemgmt.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.thasrifa.workforcemgmt.workforcemgmt.model.enums.Priority;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskPriorityUpdateRequest {
    private Long taskId;
    private Priority priority;
}
