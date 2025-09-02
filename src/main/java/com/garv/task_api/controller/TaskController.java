package com.garv.task_api.controller;

import com.garv.task_api.TaskRequest;
import com.garv.task_api.TaskResponse;
import com.garv.task_api.TaskListResponse;
import com.garv.task_api.model.Task;
import com.garv.task_api.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "APIs for managing user tasks with full CRUD operations")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final TaskService service;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public TaskResponse create(@Valid @RequestBody TaskRequest req, Authentication auth) {
        return service.create(auth.getName(), req);
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves paginated list of tasks for the authenticated user with optional status filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public TaskListResponse list(
            @Parameter(description = "Filter tasks by status") @RequestParam(required=false) Task.Status status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue="0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue="10") int size,
            Authentication auth) {
        return service.list(auth.getName(), status, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found and returned"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public TaskResponse get(@Parameter(description = "Task ID") @PathVariable String id, Authentication auth) {
        return service.get(auth.getName(), id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public TaskResponse update(@Parameter(description = "Task ID") @PathVariable String id,
                              @Valid @RequestBody TaskRequest req, Authentication auth) {
        return service.update(auth.getName(), id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a specific task for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public void delete(@Parameter(description = "Task ID") @PathVariable String id, Authentication auth) {
        service.delete(auth.getName(), id);
    }
}
