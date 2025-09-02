package com.garv.task_api.repository;

import com.garv.task_api.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    Page<Task> findByOwnerId(String ownerId, Pageable pageable);
    Page<Task> findByOwnerIdAndStatus(String ownerId, Task.Status status, Pageable pageable);
}
