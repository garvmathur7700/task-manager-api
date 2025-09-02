package com.garv.task_api;

import com.garv.task_api.model.Task;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class TaskRequest {
    @NotBlank @Size(max=120) private String title;
    @Size(max=1000) private String description;
    @NotNull private Task.Status status;
    private LocalDate dueDate;
}
