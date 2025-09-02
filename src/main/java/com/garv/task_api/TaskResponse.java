package com.garv.task_api;

import com.garv.task_api.model.Task;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private Task.Status status;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;
}