package com.garv.task_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private Status status;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;

    public enum Status { TODO, IN_PROGRESS, DONE }
}
