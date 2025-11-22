package com.example.todolistapi.service;

import com.example.todolistapi.dto.TaskRequest;
import com.example.todolistapi.dto.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getAllTasks();
    Page<TaskResponse> getAllTasksPaginated(Pageable pageable);
    TaskResponse updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);
}
