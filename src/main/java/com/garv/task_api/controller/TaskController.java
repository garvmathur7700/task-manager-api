package com.garv.task_api.controller;

import com.garv.task_api.TaskRequest;
import com.garv.task_api.TaskResponse;
import com.garv.task_api.TaskListResponse;
import com.garv.task_api.model.Task;
import com.garv.task_api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    public TaskResponse create(@Valid @RequestBody TaskRequest req, Authentication auth) {
        return service.create(auth.getName(), req);
    }

    @GetMapping
    public TaskListResponse list(@RequestParam(required=false) Task.Status status,
                                @RequestParam(defaultValue="0") int page,
                                @RequestParam(defaultValue="10") int size,
                                Authentication auth) {
        return service.list(auth.getName(), status, page, size);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable String id, Authentication auth) {
        return service.get(auth.getName(), id);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable String id, @Valid @RequestBody TaskRequest req, Authentication auth) {
        return service.update(auth.getName(), id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id, Authentication auth) {
        service.delete(auth.getName(), id);
    }
}
