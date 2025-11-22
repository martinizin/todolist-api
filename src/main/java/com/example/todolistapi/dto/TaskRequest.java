package com.example.todolistapi.dto;

import com.example.todolistapi.model.Task.Status;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TaskRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;
    @NotNull(message = "El estado es obligatorio")
    private Status status;
    @Future(message = "La fecha de vencimiento debe ser futura")
    private LocalDate dueDate;
    // Getters y Setters
}
