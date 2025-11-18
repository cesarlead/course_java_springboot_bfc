package com.cesarlead.testing.service;

import com.cesarlead.testing.dto.TaskDto;
import com.cesarlead.testing.exception.TaskNotFoundException;
import com.cesarlead.testing.model.Task;
import com.cesarlead.testing.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskDto(task.getId(), task.getTitle(), task.isCompleted()))
                .toList();
    }

    public TaskDto findById(Long id) {
        return taskRepository.findById(id)
                .map(task -> new TaskDto(task.getId(), task.getTitle(), task.isCompleted()))
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public TaskDto save(TaskDto taskDto) {
        Task task = new Task(taskDto.id(), taskDto.title(), taskDto.completed());

        Task tareaGuardada = taskRepository.save(task);

        return new TaskDto(
                tareaGuardada.getId(),
                tareaGuardada.getTitle(),
                tareaGuardada.isCompleted()
        );
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        task.setTitle(taskDto.title());
        task.setCompleted(taskDto.completed());
        taskRepository.save(task);
        return new TaskDto(task.getId(), task.getTitle(), task.isCompleted());
    }

    public void markTaskAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        task.setCompleted(true);
        taskRepository.save(task);
    }
}
