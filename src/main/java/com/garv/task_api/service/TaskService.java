package com.garv.task_api.service;

import com.garv.task_api.exception.Forbidden;
import com.garv.task_api.exception.NotFound;
import com.garv.task_api.TaskRequest;
import com.garv.task_api.TaskResponse;
import com.garv.task_api.TaskListResponse;
import com.garv.task_api.model.Task;
import com.garv.task_api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.Instant;


@Service @RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repo;


    public TaskResponse create(String ownerId, TaskRequest req) {
        var now = Instant.now();
        var saved = repo.save(Task.builder()
                .ownerId(ownerId)
                .title(req.getTitle())
                .description(req.getDescription())
                .status(req.getStatus())
                .dueDate(req.getDueDate())
                .createdAt(now).updatedAt(now)
                .build());
        return toResp(saved);
    }


    public TaskListResponse list(String ownerId, Task.Status status, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var result = (status == null)
                ? repo.findByOwnerId(ownerId, pageable)
                : repo.findByOwnerIdAndStatus(ownerId, status, pageable);

        var tasks = result.getContent().stream()
                .map(this::toResp)
                .toList();

        return new TaskListResponse(
                tasks,
                result.getTotalElements(),
                result.getNumber(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }


    public TaskResponse get(String ownerId, String id) {
        var task = repo.findById(id).orElseThrow(() -> new NotFound("Task not found"));
        if (!task.getOwnerId().equals(ownerId)) throw new Forbidden("Not your task");
        return toResp(task);
    }


    public TaskResponse update(String ownerId, String id, TaskRequest req) {
        var t = repo.findById(id).orElseThrow(() -> new NotFound("Task not found"));
        if (!t.getOwnerId().equals(ownerId)) throw new Forbidden("Not your task");
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setStatus(req.getStatus());
        t.setDueDate(req.getDueDate());
        t.setUpdatedAt(Instant.now());
        return toResp(repo.save(t));
    }


    public void delete(String ownerId, String id) {
        var t = repo.findById(id).orElseThrow(() -> new NotFound("Task not found"));
        if (!t.getOwnerId().equals(ownerId)) throw new Forbidden("Not your task");
        repo.delete(t);
    }


    private TaskResponse toResp(Task t) {
        return TaskResponse.builder()
                .id(t.getId()).title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .dueDate(t.getDueDate())
                .createdAt(t.getCreatedAt()).updatedAt(t.getUpdatedAt())
                .build();
    }
}
