package com.example.todolistapi.service.impl;

import com.example.todolistapi.dto.TaskRequest;
import com.example.todolistapi.dto.TaskResponse;
import com.example.todolistapi.exception.ResourceNotFoundException;
import com.example.todolistapi.mapper.TaskMapper;
import com.example.todolistapi.model.Task;
import com.example.todolistapi.repository.TaskRepository;
import com.example.todolistapi.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskServiceImpl(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public TaskResponse createTask(TaskRequest request) {
        Task task = mapper.toEntity(request);
        return mapper.toResponse(repository.save(task));
    }
    public TaskResponse getTaskById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        return mapper.toResponse(task);
    }
    public List<TaskResponse> getAllTasks() {
        return repository.findAll().stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }
    public Page<TaskResponse> getAllTasksPaginated(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        mapper.updateEntityFromRequest(request, task);
        return mapper.toResponse(repository.save(task));
    }
    public void deleteTask(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Tarea no encontrada con id: " + id);
        repository.deleteById(id);
    }
}
